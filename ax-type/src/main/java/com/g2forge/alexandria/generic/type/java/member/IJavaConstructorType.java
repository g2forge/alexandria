package com.g2forge.alexandria.generic.type.java.member;

import java.lang.reflect.Constructor;

import com.g2forge.alexandria.generic.type.java.IJavaInvocableType;

public interface IJavaConstructorType extends IJavaMemberType, IJavaInvocableType {
	@Override
	public Constructor<?> getJavaMember();
}
