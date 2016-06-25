package com.g2forge.alexandria.generic.type.java.type;

import com.g2forge.alexandria.generic.type.IType;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.IJavaUntype;

public interface IJavaType extends IJavaUntype, IType {
	@Override
	public IJavaType eval(ITypeEnvironment environment);

	@Override
	public IJavaConcreteType toConcrete();
}
