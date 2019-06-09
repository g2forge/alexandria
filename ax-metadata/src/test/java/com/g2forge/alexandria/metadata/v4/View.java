package com.g2forge.alexandria.metadata.v4;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.g2forge.alexandria.metadata.v4.ViewAnnotation;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Views.class)
public @interface View {
	public String value();

	@ViewAnnotation
	public Class<?>view();
}
