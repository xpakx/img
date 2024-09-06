package io.github.xpakx.images_cdc.debezium.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"created_at"})
public record Image(
        Long id,
        @JsonProperty("image_url") String imageUrl,
        String caption,
        @JsonProperty("user_id") String userId
        ) {
}
