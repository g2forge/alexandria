package com.g2forge.alexandria.generic.type.java.member;

import java.lang.reflect.Field;

import com.g2forge.alexandria.generic.type.java.type.IJavaType;

public interface IJavaFieldType extends IJavaMemberType {
	@Override
	public Field getJavaMember();

	public IJavaType getFieldType();
}
