package com.finance.user_service.dto;

import java.util.UUID;

public record UserContext(
        UUID userId,
        String accessToken,
        String refreshToken
) {}
