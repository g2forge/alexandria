package com.g2forge.alexandria.generic.type.java;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;

public interface IJavaClassType extends IJavaType, IJavaParameterizedType {
	@Override
	public IJavaClassType eval(ITypeEnvironment environment);
}
