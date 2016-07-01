package com.g2forge.alexandria.generic.type.java.type;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.IJavaParameterizedUntype;

public interface IJavaClassType extends IJavaConcreteType, IJavaParameterizedUntype {
	@Override
	public IJavaClassType eval(ITypeEnvironment environment);
}
