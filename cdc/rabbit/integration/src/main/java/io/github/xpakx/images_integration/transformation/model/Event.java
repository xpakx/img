package io.github.xpakx.images_integration.transformation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record Event<T>(T before, T after, @JsonIgnore String tableName) {
}
