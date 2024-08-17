package io.github.xpakx.images.account.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationRequestTest {
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

    private boolean hasEmptyError(Set<ConstraintViolation<RegistrationRequest>> violations, Field field) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .map(String::toLowerCase)
                .anyMatch(msg -> msg.contains(field.getName()) && msg.contains("empty"));
    }

    private boolean hasLengthError(Set<ConstraintViolation<RegistrationRequest>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .map(String::toLowerCase)
                .anyMatch(msg -> msg.contains("length"));
    }

    private boolean hasMatchError(Set<ConstraintViolation<RegistrationRequest>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .map(String::toLowerCase)
                .anyMatch(msg -> msg.contains("match"));
    }

    private String createUsernameOfLength(int length) {
        return "a".repeat(length);
    }

    @Test
    public void nullUsernameShouldFailValidation() {
        var request = new RegistrationRequest(null, "password", "password");
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(hasEmptyError(violations, Field.Username));
    }

    @Test
    public void emptyUsernameShouldFailValidation() {
        var request = new RegistrationRequest("", "password", "password");
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(hasEmptyError(violations, Field.Username));
    }

    @Test
    public void whitespaceUsernameShouldFailValidation() {
        var request = new RegistrationRequest("    ", "password", "password");
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(hasEmptyError(violations, Field.Username));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 4, 16, 20, 30, 40})
    public void usernameOfIncorrectLengthShortUsernameShouldFailValidation(int usernameLength) {
        var request = new RegistrationRequest(
                createUsernameOfLength(usernameLength), "password", "password"
        );
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(hasLengthError(violations));
    }

    @Test
    public void nullPasswordShouldFailValidation() {
        var request = new RegistrationRequest("username", null, null);
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(hasEmptyError(violations, Field.Password));
    }

    @Test
    public void emptyPasswordShouldFailValidation() {
        var request = new RegistrationRequest("username", "", "");
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(hasEmptyError(violations, Field.Password));
    }

    @Test
    public void whitespacePasswordShouldFailValidation() {
        var request = new RegistrationRequest("username", "   ", "   ");
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(hasEmptyError(violations, Field.Password));
    }

    @Test
    public void nonMatchingPasswordsShouldFailValidation() {
        var request = new RegistrationRequest("username", "password", "password2");
        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(hasMatchError(violations));
    }

    @Test
    public void correctRequestShouldPassValidation() {
        var request = new RegistrationRequest("username", "password", "password");
        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }
}