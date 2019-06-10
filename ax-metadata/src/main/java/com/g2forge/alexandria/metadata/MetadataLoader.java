package com.g2forge.alexandria.metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.g2forge.alexandria.metadata.load.IMetadataLoader;

@Retention(RetentionPolicy.RUNTIME)
public @interface MetadataLoader {
	public Class<? extends IMetadataLoader> value();
}
