package com.finance.user_service.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.expiration}")
    private int accessExpirationMs;

    @Value("${jwt.refreshExpirationMs}")
    private int refreshExpirationMs;

    private final KeyPair keyPair = Jwts.SIG.RS256.keyPair().build();

    public String generateToken(String userId) {
        return buildToken(userId, accessExpirationMs);
    }

    public String generateRefreshToken(String userId) {
        return buildToken(userId, refreshExpirationMs);
    }

    private String buildToken(String userId, int expiration) {
        return Jwts.builder()
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(keyPair.getPrivate(), Jwts.SIG.RS256)
                .compact();
    }

    public String extractUserId(String token) {
        return Jwts.parser()
                .verifyWith(keyPair.getPublic())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(keyPair.getPublic()) // Verify with PUBLIC key
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Map<String, Object> getJwks() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        return Map.of(
                "kty", "RSA",
                "use", "sig",
                "alg", "RS256",
                "n", Base64.getUrlEncoder().withoutPadding().encodeToString(publicKey.getModulus().toByteArray()),
                "e", Base64.getUrlEncoder().withoutPadding().encodeToString(publicKey.getPublicExponent().toByteArray())
        );
    }
}