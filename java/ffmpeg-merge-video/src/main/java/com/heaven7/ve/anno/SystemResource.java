package com.heaven7.ve.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * the system resource means it will only init once.
 * @author heaven7
 */
@Retention(RetentionPolicy.CLASS)
public @interface SystemResource {
}
