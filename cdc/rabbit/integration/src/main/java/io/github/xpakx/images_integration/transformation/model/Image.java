package io.github.xpakx.images_integration.transformation.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Image(
        Long id,
        @JsonProperty("image_url") String imageUrl,
        String caption,
        @JsonProperty("user_id") String userId,
        @JsonProperty("created_at") String created_at
) {
}
