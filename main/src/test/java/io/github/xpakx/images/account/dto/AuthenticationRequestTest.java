package io.github.xpakx.images.account.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationRequestTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Getter
    @RequiredArgsConstructor
    private enum Field {
        Username("username"), Password("password");
        private final String name;
    }

    private boolean hasEmptyError(Set<ConstraintViolation<AuthenticationRequest>> violations, Field field) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .map(String::toLowerCase)
                .anyMatch(msg -> msg.contains(field.getName()) && msg.contains("empty"));
    }

    @Test
    public void nullUsernameShouldFailValidation() {
        var request = new AuthenticationRequest(null, "password");
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(hasEmptyError(violations, Field.Username));
    }

    @Test
    public void emptyUsernameShouldFailValidation() {
        var request = new AuthenticationRequest("", "password");
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(hasEmptyError(violations, Field.Username));
    }

    @Test
    public void whitespaceUsernameShouldFailValidation() {
        var request = new AuthenticationRequest("    ", "password");
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(hasEmptyError(violations, Field.Username));
    }

    @Test
    public void nullPasswordShouldFailValidation() {
        var request = new AuthenticationRequest("username", null);
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(hasEmptyError(violations, Field.Password));
    }

    @Test
    public void emptyPasswordShouldFailValidation() {
        var request = new AuthenticationRequest("username", "");
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(hasEmptyError(violations, Field.Password));
    }

    @Test
    public void whitespacePasswordShouldFailValidation() {
        var request = new AuthenticationRequest("username", "    ");
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(hasEmptyError(violations, Field.Password));
    }

    @Test
    public void correctRequestShouldPassValidation() {
        var request = new AuthenticationRequest("username", "password");
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }
}
