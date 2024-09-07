package io.github.xpakx.images_cdc.data.dto;

import java.util.UUID;

public record EventData(UUID id, String name, Long userId) {
}
