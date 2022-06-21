package com.ljf.ploughthewaves.infrastructure.util.ratelimit;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    String key() default "PloughTheWaves";
    int time() default 30;
    int count() default 50;
    LimitType limitType() default LimitType.DEFAULT;
}
