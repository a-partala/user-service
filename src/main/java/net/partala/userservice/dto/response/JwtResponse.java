package net.partala.userservice.dto.response;

import java.time.Instant;

public record JwtResponse(
        String token,
        String tokenType,
        Instant expiresAt
) {
}
