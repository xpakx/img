package io.github.xpakx.images.account;

import io.github.xpakx.images.account.dto.AuthenticationRequest;
import io.github.xpakx.images.account.dto.AuthenticationResponse;
import io.github.xpakx.images.account.dto.RegistrationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegistrationRequest registrationRequest) {
        return new ResponseEntity<>(
                service.register(registrationRequest),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(
                service.generateAuthenticationToken(authenticationRequest)
        );
    }
}
