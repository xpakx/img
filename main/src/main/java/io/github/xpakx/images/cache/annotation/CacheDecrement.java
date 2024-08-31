package io.github.xpakx.images.cache.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CacheDecrements.class)
public @interface CacheDecrement {
    String value();
    String key() default "";
}
