package io.github.xpakx.images.priv.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record MessageRequest(
        @NotEmpty(message = "Message cannot be empty")
        String content,
        @NotEmpty(message = "Recipient must be specified")
        String recipient
) {
}
