package io.github.xpakx.images.cache.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CacheIncrements.class)
public @interface CacheIncrement {
    String value();
    String key() default "";
}
