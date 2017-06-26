package com.g2forge.alexandria.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Handler {
	public Class<? extends IAnnotationHandler<?>> value();
}
