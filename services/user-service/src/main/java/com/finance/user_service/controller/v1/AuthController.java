package com.finance.user_service.controller.v1;

import com.finance.user_service.dto.SignUpRequest;
import com.finance.user_service.dto.SignInRequest;
import com.finance.user_service.dto.UserContext;
import com.finance.user_service.exception.AuthException;
import com.finance.user_service.model.RefreshToken;
import com.finance.user_service.model.TransactionUser;
import com.finance.user_service.service.AuthService;
import com.finance.user_service.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController()
@RequestMapping("api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;
    private RefreshTokenService refreshTokenService;

    @PostMapping("/registration")
    public ResponseEntity<UserContext> registerUser(@RequestBody SignUpRequest signUpRequest) {
        TransactionUser user = authService.register(signUpRequest);

        String accessToken = authService.getAccessToken(user.getId());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return ResponseEntity.ok(
                new UserContext(user.getId(), accessToken, refreshToken.getToken())
        );
    }

    @PostMapping("/login")
    public ResponseEntity<UserContext> login(@RequestBody SignInRequest signInRequest) {
        TransactionUser user = authService.authenticate(signInRequest);

        String accessToken = authService.getAccessToken(user.getId());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return ResponseEntity.ok(
                new UserContext(user.getId(), accessToken, refreshToken.getToken())
        );
    }

    @PutMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody UserContext userContext) {
        TransactionUser user = refreshTokenService.validateAndGetOwner(
                userContext.refreshToken(), userContext.userId()
        );

        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);
        String newAccessToken = authService.generateTokenForUser(user);

        return ResponseEntity.ok(new UserContext(
                user.getId(),
                newAccessToken,
                newRefreshToken.getToken()
        ));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody UserContext userContext) {
        refreshTokenService.deleteRefreshToken(userContext.userId());
        return ResponseEntity.ok().body("Logged out successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok("Your ID is: " + userId);
    }

    @PostMapping("/silent")
    public ResponseEntity<?> silentAuth(@RequestBody UserContext userContext) {
        // 1. Lightweight check - No DB hit
        if (authService.authenticate(userContext.accessToken())) {
            return ResponseEntity.ok().build();
        }

        // 2. Access token expired - Now we hit the DB for the Refresh Token
        try {
            refreshTokenService.validateAndGetOwner(
                    userContext.refreshToken(),
                    userContext.userId()
            );

            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Refresh required");
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired");
        }
    }

    @GetMapping("/.well-known/jwks.json")
    public ResponseEntity<Map<String, Object>> getPublicKey() {
        return ResponseEntity.ok(authService.getPublicKey());
    }
}
