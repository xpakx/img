package io.github.xpakx.images_integration.transformation.model;

public record Event<T>(T before, T after, String tableName) {
}