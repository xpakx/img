package io.github.xpakx.images.image.dto;

import java.time.LocalDateTime;

public record ImageDetails(
        String id,
        String caption,
        String imageUrl,
        LocalDateTime createdAt,
        String author,
        String avatarUrl,
        Long likes,
        Long comments,
        boolean liked,
        boolean owner
) {
}
