package com.finance.user_service.service;

import com.finance.user_service.exception.AuthException;
import com.finance.user_service.model.RefreshToken;
import com.finance.user_service.model.TransactionUser;
import com.finance.user_service.repository.RefreshTokenRepository;
import com.finance.user_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtUtil jwtUtil;

    @Value("${jwt.refreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Transactional
    public RefreshToken createRefreshToken(TransactionUser user) {
        // No more findById query!
        refreshTokenRepository.deleteByUserId(user.getId());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(jwtUtil.generateRefreshToken(user.getId().toString()));

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public TransactionUser validateAndGetOwner(String token, UUID claimedUserId) {
        RefreshToken refreshToken = refreshTokenRepository.findByTokenWithUser(token)
                .orElseThrow(() -> new AuthException("Invalid Refresh Token"));

        // 1. Verify Ownership
        if (!refreshToken.getUser().getId().equals(claimedUserId)) {
            throw new AuthException("Token hijacking detected!");
        }

        // 2. Verify Expiration
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new AuthException("Refresh token expired");
        }

        // 3. Verify Signature
        if (!jwtUtil.validateToken(token)) {
            throw new AuthException("Invalid token signature");
        }

        return refreshToken.getUser(); // Return the user we already fetched!
    }

    @Transactional
    public void deleteRefreshToken(UUID userID) {
        refreshTokenRepository.deleteByUserId(userID);
    }

    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void purgeExpiredTokens() {
        Instant now = Instant.now();
        // This cleans up the database based on the expiry_date column
        refreshTokenRepository.deleteByExpiryDateBefore(now);
    }
}