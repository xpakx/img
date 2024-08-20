package io.github.xpakx.images.common.types;

public sealed interface Result<T> permits Result.Ok, Result.Err {
    record Ok<T>(T value) implements Result<T> {}
    record Err<T>(Exception e) implements Result<T> {}

    default boolean isOk() {
        return this instanceof Ok<?>;
    }

    default T unwrap() {
        return switch (this) {
            case Ok<T> ok -> ok.value();
            case Err<T> _ -> throw new RuntimeException("Cannot unwrap error");
        };
    }
}
