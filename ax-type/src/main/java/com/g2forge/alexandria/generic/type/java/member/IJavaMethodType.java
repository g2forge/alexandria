package com.g2forge.alexandria.generic.type.java.member;

import java.lang.reflect.Method;

public interface IJavaMethodType extends IJavaMemberType {
	@Override
	public Method getJavaMember();
}
