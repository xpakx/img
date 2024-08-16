package io.github.xpakx.images.account.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

public record RegistrationRequest (
        @NotBlank(message = "Username cannot be empty")
        @Length(min=5, max=15, message = "Username length must be between 5 and 15")
        String username,

        @NotBlank(message = "Password cannot be empty")
        String password,
        String passwordRe) {

    @AssertTrue(message = "Passwords don't match!")
    boolean isPasswordRepeated() {
        return Objects.equals(password, passwordRe);
    }
}
