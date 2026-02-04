package net.partala.userservice.auth.email;

import net.partala.userservice.auth.SecurityUser;
import net.partala.userservice.dto.response.JwtResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {


    private final Logger log = LoggerFactory.getLogger(EmailController.class);
    private final EmailService emailService;

    public EmailController(
            EmailService emailService
    ) {
        this.emailService = emailService;
    }

    //todo: actually send verification
    @PostMapping("request-verification")
    public ResponseEntity<JwtResponse> sendEmailVerification(
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        log.info("Called sendEmailVerification");

        var jwtResponse = emailService.getEmailToken(securityUser);
        return ResponseEntity.status(HttpStatus.OK)
                .body(jwtResponse);
    }

    @PostMapping("verify")
    public ResponseEntity<Void> verifyEmail(
            @RequestParam(value = "token", required = true)
            String token
    ) {
        log.info("Called verifyEmail");

        emailService.verifyEmail(token);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
