package com.g2forge.alexandria.generic.type.java;

import java.lang.reflect.Method;

public interface IJavaMethodType extends IJavaMemberType {
	@Override
	public Method getJavaMember();
}
