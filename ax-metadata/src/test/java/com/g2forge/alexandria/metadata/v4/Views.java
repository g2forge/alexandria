package com.g2forge.alexandria.metadata.v4;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Views {
	public View[] value();
}
