package com.g2forge.alexandria.generic.type.java;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.structure.IJavaClassStructure;

public interface IJavaClassType extends IJavaType, IJavaParameterizedType, IJavaClassStructure<IJavaClassType, IJavaFieldType> {
	@Override
	public IJavaClassType eval(ITypeEnvironment environment);
}
