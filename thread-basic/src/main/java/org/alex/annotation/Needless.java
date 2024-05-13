package org.alex.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author wangzifeng
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Needless {

    @AliasFor(value = "name")
    String value() default "";

    @AliasFor(value = "value")
    String name() default "";
}
