package com.solan.jbase.log.annotation;

/**
 * TODO:
 *
 * @author: hyl
 * @date: 2020/3/30 18:01
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface LogActive {
    String value() default "";

    String project() default "";

    String product() default "";

    String module() default "";

    String fun() default "";

    String optType() default "";

    String level() default "";
}
