package com.g2forge.alexandria.generic.type.java.member;

import java.lang.reflect.Constructor;
import java.util.List;

import com.g2forge.alexandria.generic.type.java.type.IJavaType;

public interface IJavaConstructorType extends IJavaMemberType {
	@Override
	public Constructor<?> getJavaMember();

	public List<IJavaType> getParameterTypes();
}
