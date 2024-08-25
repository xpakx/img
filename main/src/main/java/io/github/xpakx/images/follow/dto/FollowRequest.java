package io.github.xpakx.images.follow.dto;

import jakarta.validation.constraints.NotBlank;

public record FollowRequest(
        @NotBlank(message = "Username cannot be empty")
        String username
) {
}
