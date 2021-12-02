package com.zyx.crackgameserver.logging;


import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface logAnnotation {
    String module() default "";
    String operator() default "";
}
