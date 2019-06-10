package com.g2forge.alexandria.metadata.viewdemo;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(View.Views.class)
public @interface View {
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Views {
		public View[] value();
	}
	
	public String value();

	@ViewAnnotation
	public Class<?>view();
}
