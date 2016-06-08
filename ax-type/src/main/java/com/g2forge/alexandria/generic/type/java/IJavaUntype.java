package com.g2forge.alexandria.generic.type.java;

import java.lang.reflect.Type;

import com.g2forge.alexandria.generic.type.IType;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;

public interface IJavaUntype extends IType {
	@Override
	public IJavaUntype eval(ITypeEnvironment environment);
	
	public Type getJavaType();
	
	public boolean isEnum();
}
