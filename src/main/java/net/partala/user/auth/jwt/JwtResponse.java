package net.partala.user.auth.jwt;

import java.time.Instant;

public record JwtResponse(
        String token,
        String tokenType,
        Instant expiresAt
) {
}
