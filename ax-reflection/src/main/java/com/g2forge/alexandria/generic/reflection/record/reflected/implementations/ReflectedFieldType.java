package com.g2forge.alexandria.generic.reflection.record.reflected.implementations;

import com.g2forge.alexandria.generic.record.IField;
import com.g2forge.alexandria.generic.reflection.annotations.IJavaAnnotations;
import com.g2forge.alexandria.generic.reflection.object.IJavaFieldReflection;
import com.g2forge.alexandria.generic.reflection.record.reflected.IReflectedFieldType;
import com.g2forge.alexandria.generic.type.java.IJavaUntype;

public class ReflectedFieldType<R, T> implements IReflectedFieldType<R, T> {
	protected final IJavaFieldReflection<R, T> field;
	
	/**
	 * @param field
	 */
	public ReflectedFieldType(final IJavaFieldReflection<R, T> field) {
		this.field = field;
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
	
	@Override
	public IField<R, T> map(final R input) {
		return new ReflectedField<R, T>(this, input);
	}
}
