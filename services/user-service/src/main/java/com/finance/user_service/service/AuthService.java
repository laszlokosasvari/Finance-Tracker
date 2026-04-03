package com.finance.user_service.service;

import com.finance.user_service.dto.SignInRequest;
import com.finance.user_service.dto.SignUpRequest;
import com.finance.user_service.exception.AuthException;
import com.finance.user_service.model.TransactionUser;
import com.finance.user_service.repository.TransactionUserRepository;
import com.finance.user_service.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final TransactionUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public TransactionUser authenticate(SignInRequest request) {
        TransactionUser user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthException("Invalid credentials"));

        if (user.isAccount_locked()) {
            // 1. Calculate the 'Release' moment: Incident + 15 mins
            Instant lockIncident = user.getLock_time().toInstant();
            Instant releaseTime = lockIncident.plus(Duration.ofMinutes(15));

            // 2. Check if the 'Release' moment is in the past
            if (Instant.now().isAfter(releaseTime)) {
                // Cooldown finished - Lift the lock
                user.setAccount_locked(false);
                user.setFailed_attempts(0);
                user.setLock_time(null);
                // Don't return yet! Let them try the password they just provided
            } else {
                // Still locked
                throw new AuthException("Account locked. Try again after " + releaseTime);
            }
        }

        // 3. Password Verification
        if (!passwordEncoder.matches(request.password(), user.getPassword_hash())) {
            int attempts = user.getFailed_attempts() + 1;
            user.setFailed_attempts(attempts);

            if (attempts >= 5) {
                user.setAccount_locked(true);
                user.setLock_time(Timestamp.from(Instant.now()));
            }
            userRepository.save(user);
            throw new AuthException("Invalid credentials");
        }

        // 3. Success: Reset everything
        user.setFailed_attempts(0);
        user.setAccount_locked(false);
        user.setLock_time(null);
        userRepository.save(user);

        return user;
    }

    public boolean authenticate(String accessToken) {
        return jwtUtil.validateToken(accessToken);
    }

    @Transactional
    public TransactionUser register(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.email())) {
            throw new AuthException("User already exists!");
        }

        String hashedPwd = passwordEncoder.encode(signUpRequest.password());
        TransactionUser user = signUpRequest.mapToEntity(hashedPwd);

        return userRepository.save(user); // Return the saved entity
    }

    // Keep the ID version for other uses
    public String getAccessToken(UUID userID) {
        if (userRepository.existsById(userID)) {
            return jwtUtil.generateToken(userID.toString());
        }
        throw new AuthException("User not found");
    }

    // Add this for high-performance refresh
    public String generateTokenForUser(TransactionUser user) {
        return jwtUtil.generateToken(user.getId().toString());
    }

    public Map<String, Object> getPublicKey() {
       return jwtUtil.getJwks();
    }
}
