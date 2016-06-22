package com.g2forge.alexandria.generic.type.java;

import java.lang.reflect.Constructor;
import java.util.List;

public interface IJavaConstructorType extends IJavaMemberType {
	@Override
	public Constructor<?> getJavaMember();

	public List<IJavaType> getParameterTypes();
}
