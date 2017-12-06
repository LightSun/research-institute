package com.heaven7.java.study;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Modifier;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Getter {

    int value() default Modifier.PUBLIC;
}
