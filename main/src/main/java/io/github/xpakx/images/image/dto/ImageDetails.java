package io.github.xpakx.images.image.dto;

import java.time.LocalDateTime;

public record ImageDetails(
        String id,
        String caption,
        LocalDateTime createdAt,
        String author,
        Long likes,
        Long comments,
        boolean liked,
        boolean owner
) {
}
