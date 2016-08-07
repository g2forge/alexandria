package com.g2forge.alexandria.reflection.object;

import com.g2forge.alexandria.generic.type.java.type.IJavaType;
import com.g2forge.alexandria.reflection.object.implementations.JavaConcreteReflection;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public abstract class AJavaTypeReflection<T, Y extends IJavaType> implements IJavaTypeReflection<T> {
	protected final Y type;

	public AJavaTypeReflection(Y type) {
		this.type = type;
	}

	@Override
	public IJavaConcreteReflection<? super T> erase() {
		return new JavaConcreteReflection<>(type.erase());
	}

	@SuppressWarnings("unchecked")
	protected Class<T> getInternal() {
		return (Class<T>) type.getJavaType();
	}

	@Override
	public IJavaType getType() {
		return type;
	}
}
