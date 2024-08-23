package io.github.xpakx.images.like.dto;

import jakarta.validation.constraints.NotBlank;

public record LikeRequest(
        @NotBlank(message = "Image id cannot be empty")
        String imageId
) {
}
