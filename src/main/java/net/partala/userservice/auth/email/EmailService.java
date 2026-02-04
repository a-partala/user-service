package net.partala.userservice.auth.email;

import net.partala.userservice.auth.AuthService;
import net.partala.userservice.auth.SecurityUser;
import net.partala.userservice.auth.jwt.JwtService;
import net.partala.userservice.auth.jwt.TokenPurpose;
import net.partala.userservice.dto.response.JwtResponse;
import net.partala.userservice.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class EmailService {


    private final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserService userService;
    private final JwtService jwtService;

    public EmailService(
            UserService userService,
            JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public JwtResponse getEmailToken(
            SecurityUser securityUser,
            String email
    ) {
        log.info("Called getEmailToken");

        var jwt = jwtService.generateEmailVerificationToken(securityUser, email);
        var expiresAt = Instant.now().plus(Duration.ofMinutes(jwtService.getExpirationMinutes()));
        return new JwtResponse(
                jwt,
                "Bearer",
                expiresAt
        );
    }

    public void verifyEmail(
            String token
    ) {

        try {
            TokenPurpose purpose = jwtService.extractPurpose(token);
            if(purpose != TokenPurpose.EMAIL_VERIFICATION) {
                throw new AccessDeniedException("Invalid token format or signature");
            }
            String email = jwtService.extractEmail(token);
            Long userId = jwtService.extractUserId(token);
            userService.verifyEmail(userId, email);
        } catch (Exception e) {
            throw new AccessDeniedException("Invalid token format or signature");
        }

    }
}
