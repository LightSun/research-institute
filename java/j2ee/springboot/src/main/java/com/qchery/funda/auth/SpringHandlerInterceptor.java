package com.qchery.funda.auth;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * spring HandlerInterceptor扩展
 *
 * @author sdcuike
 *     <p>Created on 2017.02.13
 *     <p>自定义注解，处理Spring HandlerInterceptor，执行顺序按order 升序排序
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface SpringHandlerInterceptor {
    String[] value() default {};

    String[] includePatterns() default {};

    String[] excludePatterns() default {};

    int order() default Ordered.LOWEST_PRECEDENCE;
}
