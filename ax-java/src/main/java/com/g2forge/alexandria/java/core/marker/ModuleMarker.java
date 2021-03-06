package com.g2forge.alexandria.java.core.marker;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates that a type (generally an interface) marks the root package of a module.
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface ModuleMarker {}
