package com.g2forge.alexandria.metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MetadataLoader {
	public Class<? extends IMetadataLoader> value();
}
