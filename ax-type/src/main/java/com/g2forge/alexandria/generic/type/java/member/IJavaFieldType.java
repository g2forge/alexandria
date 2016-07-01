package com.g2forge.alexandria.generic.type.java.member;

import java.lang.reflect.Field;

import com.g2forge.alexandria.generic.type.java.type.IJavaType;
import com.g2forge.alexandria.generic.type.java.typed.IJavaTyped;

public interface IJavaFieldType extends IJavaMemberType, IJavaTyped {
	@Override
	public Field getJavaMember();

	@Override
	public IJavaType getType();
}
