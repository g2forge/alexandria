package com.g2forge.alexandria.generic.type.java.member;

import java.lang.reflect.Method;

import com.g2forge.alexandria.generic.type.java.IJavaInvocableType;
import com.g2forge.alexandria.generic.type.java.type.IJavaType;
import com.g2forge.alexandria.generic.type.java.typed.IJavaTyped;

public interface IJavaMethodType extends IJavaMemberType, IJavaInvocableType, IJavaTyped {
	@Override
	public Method getJavaMember();
	
	@Override
	public IJavaType getType();
}
