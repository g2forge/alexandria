package com.g2forge.alexandria.java.marker;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Use this annotation on types which will be compared using the identity functions.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface Identity {}
