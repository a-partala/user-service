package net.partala.userservice.auth;

import jakarta.validation.Valid;
import net.partala.userservice.dto.response.JwtResponse;
import net.partala.userservice.dto.request.LoginRequest;
import net.partala.userservice.dto.request.RegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(
            AuthService authService
    ) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody @Valid
            RegistrationRequest registrationRequest
    ) {
        log.info("Called register");

        authService.register(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @RequestBody @Valid
            LoginRequest loginRequest
    ) {
        log.info("Called login");

        var response = authService.authenticate(loginRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
