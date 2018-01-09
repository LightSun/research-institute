package test.provide.framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * indicate the fields will not be proguarded.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface KeepFields {
}
