package io.github.xpakx.images.account.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(
    @NotBlank(message = "Username cannot be empty")
    String username,
    @NotBlank(message = "Password cannot be empty")
    String password
) {}
