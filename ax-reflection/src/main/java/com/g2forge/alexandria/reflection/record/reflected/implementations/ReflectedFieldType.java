package com.g2forge.alexandria.reflection.record.reflected.implementations;

import com.g2forge.alexandria.generic.record.IField;
import com.g2forge.alexandria.generic.type.java.IJavaUntype;
import com.g2forge.alexandria.reflection.annotations.IJavaAnnotations;
import com.g2forge.alexandria.reflection.object.IJavaFieldReflection;
import com.g2forge.alexandria.reflection.record.reflected.IReflectedFieldType;

public class ReflectedFieldType<R, T> implements IReflectedFieldType<R, T> {
	protected final IJavaFieldReflection<R, T> field;
	
	/**
	 * @param field
	 */
	public ReflectedFieldType(final IJavaFieldReflection<R, T> field) {
		this.field = field;
	}
	
	@Override
	public IField<R, T> apply(final R input) {
		return new ReflectedField<R, T>(this, input);
	}
	
	@Override
	public IJavaAnnotations getAnnotations() {
		return field.getAnnotations();
	}
	
	@Override
	public String getName() {
		return field.getType().getName();
	}
	
	@Override
	public IJavaUntype getType() {
		return field.getType().getType();
	}
}
