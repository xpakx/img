package io.github.xpakx.images_cdc.debezium.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"password"})
public record Account(Long id, String username, @JsonProperty("avatar_url") String avatarUrl) {
}
