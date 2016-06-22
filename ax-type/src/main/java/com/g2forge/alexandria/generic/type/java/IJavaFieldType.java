package com.g2forge.alexandria.generic.type.java;

import java.lang.reflect.Field;

import com.g2forge.alexandria.generic.type.java.typed.IJavaTyped;

public interface IJavaFieldType extends IJavaMemberType, IJavaTyped {
	@Override
	public Field getJavaMember();

	@Override
	public IJavaType getType();
}
