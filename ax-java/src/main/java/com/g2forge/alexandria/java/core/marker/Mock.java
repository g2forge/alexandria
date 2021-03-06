package com.g2forge.alexandria.java.core.marker;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used to indicate that a type is a mock of something.
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface Mock {}
