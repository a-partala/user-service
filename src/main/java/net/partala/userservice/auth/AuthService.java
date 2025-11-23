package net.partala.userservice.auth;

import net.partala.userservice.dto.response.JwtResponse;
import net.partala.userservice.auth.jwt.JwtService;
import net.partala.userservice.auth.jwt.TokenPurpose;
import net.partala.userservice.dto.request.LoginRequest;
import net.partala.userservice.dto.request.RegistrationRequest;
import net.partala.userservice.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.Duration;
import java.time.Instant;

@org.springframework.stereotype.Service
public class AuthService {

    private final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthService(
            UserDetailsService userDetailsService,
            AuthenticationManager authenticationManager, UserService userService,
            JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public void register(RegistrationRequest registrationRequest) {
        userService.createUser(registrationRequest);
    }

    public JwtResponse authenticate(LoginRequest loginRequest) {

        var authToken = new UsernamePasswordAuthenticationToken(
                loginRequest.username(),
                loginRequest.password());
        authenticationManager.authenticate(authToken);
        var userDetails = userDetailsService.loadUserByUsername(loginRequest.username());
        var jwt = jwtService.generateToken(userDetails, TokenPurpose.ACCESS);
        var expiresAt = Instant.now().plus(Duration.ofMinutes(jwtService.getExpirationMinutes()));
        return new JwtResponse(
                jwt,
                "Bearer",
                expiresAt
        );
    }

    public JwtResponse getEmailToken(
            SecurityUser securityUser
    ) {
        log.info("Called getEmailToken");

        var jwt = jwtService.generateToken(securityUser, TokenPurpose.EMAIL_VERIFICATION);
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

        TokenPurpose purpose = jwtService.extractPurpose(token);
        if(purpose != TokenPurpose.EMAIL_VERIFICATION) {
            throw new AccessDeniedException("Token purpose is not allowed for this operation");
        }

        Long userId = jwtService.extractUserId(token);

        userService.verifyEmail(userId);
    }

}
