package com.g2forge.alexandria.metadata.v5;

import java.lang.reflect.AnnotatedElement;

import com.g2forge.alexandria.metadata.v5.load.Metadata;

public interface IMetadata {
	public static IMetadata of(AnnotatedElement element) {
		return new Metadata(element);
	}

	public static IMetadata of(AnnotatedElement element, Object value) {
		return new Metadata(element, value);
	}

	public <T> T getMetadata(Class<T> type);
}
