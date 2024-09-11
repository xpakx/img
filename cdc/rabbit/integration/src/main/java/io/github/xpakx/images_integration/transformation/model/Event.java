package io.github.xpakx.images_integration.transformation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record Event<T>(T before, T after, Operation operation, @JsonIgnore String tableName) {
}
