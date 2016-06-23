package com.g2forge.alexandria.generic.type.java;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.structure.IJavaClassStructure;

public interface IJavaType extends IJavaUntype, IJavaClassStructure<IJavaClassType, IJavaType, IJavaFieldType, IJavaMethodType, IJavaConstructorType> {
	@Override
	public IJavaType eval(ITypeEnvironment environment);
}
