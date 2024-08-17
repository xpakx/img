package io.github.xpakx.images.account.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenRequestTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    private boolean hasEmptyError(Set<ConstraintViolation<RefreshTokenRequest>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .map(String::toLowerCase)
                .anyMatch(msg -> msg.contains("token") && msg.contains("empty"));
    }

    @Test
    public void nullTokenShouldFailValidation() {
        var request = new RefreshTokenRequest(null);
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(hasEmptyError(violations));
    }

    @Test
    public void emptyTokenShouldFailValidation() {
        var request = new RefreshTokenRequest("");
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(hasEmptyError(violations));
    }

    @Test
    public void whitespaceTokenShouldFailValidation() {
        var request = new RefreshTokenRequest("    ");
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(hasEmptyError(violations));
    }

    @Test
    public void nonEmptyRequestShouldPassValidation() {
        var request = new RefreshTokenRequest("token");
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }
}