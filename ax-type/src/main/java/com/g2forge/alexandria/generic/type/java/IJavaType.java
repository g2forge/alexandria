package com.g2forge.alexandria.generic.type.java;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.structure.IJavaTypeStructure;

public interface IJavaType extends IJavaUntype, IJavaTypeStructure<IJavaClassType> {
	@Override
	public IJavaType eval(ITypeEnvironment environment);
}
