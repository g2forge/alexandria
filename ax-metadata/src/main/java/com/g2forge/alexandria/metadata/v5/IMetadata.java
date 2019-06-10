package com.g2forge.alexandria.metadata.v5;

import java.lang.reflect.AnnotatedElement;

import com.g2forge.alexandria.metadata.v5.load.AnnotatedElementElement;

public interface IMetadata {
	public static IMetadata of(AnnotatedElement element) {
		return new AnnotatedElementElement(element);
	}

	public <T> T getMetadata(Class<T> type);
}
