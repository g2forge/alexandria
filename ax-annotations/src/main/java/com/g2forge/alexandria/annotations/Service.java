package com.g2forge.alexandria.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE })
public @interface Service {
	/**
	 * The service interfaces which the annotated type supports.
	 */
	public Class<?>[] value();
}
