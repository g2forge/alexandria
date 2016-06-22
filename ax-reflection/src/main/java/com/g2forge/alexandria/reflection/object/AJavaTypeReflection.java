package com.g2forge.alexandria.reflection.object;

import com.g2forge.alexandria.generic.type.java.IJavaType;
import com.g2forge.alexandria.reflection.object.implementations.JavaClassReflection;

public abstract class AJavaTypeReflection<T, Y extends IJavaType> implements IJavaTypeReflection<T> {
	protected final Y type;
	
	/**
	 * @param type
	 */
	public AJavaTypeReflection(Y type) {
		this.type = type;
	}
	
	@Override
	public IJavaClassReflection<? super T> erase() {
		return new JavaClassReflection<>(type.erase());
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
