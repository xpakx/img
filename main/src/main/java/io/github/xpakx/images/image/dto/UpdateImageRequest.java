package io.github.xpakx.images.image.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateImageRequest(
        @NotNull(message = "Description must be specified")
        String caption
) {
}
