package com.g2forge.alexandria.java.marker;

import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used to indicate that a package contains a data model.
 */
@Retention(RUNTIME)
@Target(PACKAGE)
public @interface Model {
	/**
	 * The name of the model.
	 * 
	 * @return The name of the model.
	 */
	public String value() default "";
}
