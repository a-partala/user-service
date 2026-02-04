package net.partala.userservice.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import net.partala.userservice.auth.SecurityUser;
import net.partala.userservice.config.JwtProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    private final Logger log = LoggerFactory.getLogger(JwtService.class);
    private final JwtProperties properties;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(properties.secret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails, TokenPurpose tokenPurpose) {
        Instant now = Instant.now();
        log.info("{}", properties.expirationMinutes());
        Instant expire = now.plus(Duration.ofMinutes(properties.expirationMinutes()));

        SecurityUser user = (SecurityUser) userDetails;
        return Jwts.builder()
                .claim("userId", user.getId())
                .claim("purpose", tokenPurpose.name())
                .claim("roles", user.getRoles())
                .setSubject(user.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expire))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Long extractUserId(String token) {
        return parseAllClaims(token).get("userId", Long.class);
    }

    public TokenPurpose extractPurpose(String token) {
        String purposeStr = parseAllClaims(token).get("purpose", String.class);
        try {
            return TokenPurpose.valueOf(purposeStr);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BadCredentialsException("Invalid token format or signature");
        }
    }

    public String extractUsername(String token) {
        return parseAllClaims(token).getSubject();
    }

    public Instant extractExpiration(String token) {

        Date expire = parseAllClaims(token).getExpiration();
        return expire.toInstant();
    }

    public boolean isTokenValid(
            String token,
            UserDetails userDetails) {

        try {
            var usernameFromToken = extractUsername(token);

            return usernameFromToken.equals(userDetails.getUsername());
        } catch (ExpiredJwtException e) {
            log.debug("Token expired for user {}", userDetails.getUsername());
            return false;
        }
    }

    private Claims parseAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public long getExpirationMinutes() {
        return properties.expirationMinutes();
    }
}
