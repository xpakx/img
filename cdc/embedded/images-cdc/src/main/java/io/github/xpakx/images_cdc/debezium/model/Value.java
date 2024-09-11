package io.github.xpakx.images_cdc.debezium.model;

public record Value<T>(T before, T after, Operation operation) {
}
