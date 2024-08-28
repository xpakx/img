package io.github.xpakx.images.comment.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotEmpty(message = "Comment cannot be empty")
        @Size(min = 20, max = 250, message = "Comment length must be between 20 and 250 chars")
        String content
) {
}
