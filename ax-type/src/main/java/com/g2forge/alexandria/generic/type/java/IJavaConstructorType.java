package com.g2forge.alexandria.generic.type.java;

import java.lang.reflect.Constructor;

public interface IJavaConstructorType extends IJavaMemberType {
	@Override
	public Constructor<?> getJavaMember();
}
