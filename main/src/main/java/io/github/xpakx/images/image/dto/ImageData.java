package io.github.xpakx.images.image.dto;

import java.time.LocalDateTime;

public record ImageData(String id, String caption, LocalDateTime createdAt) {
}
