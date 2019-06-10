package com.g2forge.alexandria.reflection.record.v1.reflected.implementations;

import com.g2forge.alexandria.adt.record.IField;
import com.g2forge.alexandria.generic.type.java.IJavaUntype;
import com.g2forge.alexandria.metadata.annotation.IJavaAnnotations;
import com.g2forge.alexandria.reflection.object.IJavaFieldReflection;
import com.g2forge.alexandria.reflection.record.v1.reflected.IReflectedFieldType;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReflectedFieldType<R, T> implements IReflectedFieldType<R, T> {
	protected final IJavaFieldReflection<R, T> field;

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
		return field.getType().getFieldType();
	}
}
