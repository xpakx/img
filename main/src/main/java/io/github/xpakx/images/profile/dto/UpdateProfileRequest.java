package io.github.xpakx.images.profile.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateProfileRequest(
        @NotNull(message = "Description must be specified")
        String description
) {
}
