package com.g2forge.alexandria.java.reflection.type;

import java.lang.reflect.Type;

public interface ITypeReference<T> {
	public Type getType();

	public Class<? super T> getErased();
}
