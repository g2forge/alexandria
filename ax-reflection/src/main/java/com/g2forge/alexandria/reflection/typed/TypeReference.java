package com.g2forge.alexandria.reflection.typed;

import java.lang.reflect.Type;

import com.g2forge.alexandria.reflection.object.IJavaTypeReflection;
import com.g2forge.alexandria.reflection.object.ReflectionHelpers;

import lombok.Data;

@Data
public class TypeReference<T> implements ITypeReference<T> {
	protected final IJavaTypeReflection<T> type;

	public TypeReference(IJavaTypeReflection<T> type) {
		this.type = type;
	}

	public TypeReference(Type type) {
		this(ReflectionHelpers.toReflection(type));
	}
}
