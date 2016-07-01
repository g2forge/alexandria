package com.g2forge.alexandria.generic.type.java.member;

import java.lang.reflect.Method;

import com.g2forge.alexandria.generic.type.java.IJavaInvocableType;

public interface IJavaMethodType extends IJavaMemberType, IJavaInvocableType {
	@Override
	public Method getJavaMember();
}
