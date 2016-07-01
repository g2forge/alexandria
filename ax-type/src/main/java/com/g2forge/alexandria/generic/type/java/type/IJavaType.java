package com.g2forge.alexandria.generic.type.java.type;

import com.g2forge.alexandria.generic.type.IResolvableType;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.IJavaUntype;
import com.g2forge.alexandria.generic.type.java.structure.IJavaTypeStructure;

public interface IJavaType extends IJavaUntype, IResolvableType, IJavaTypeStructure<IJavaClassType> {
	@Override
	public IJavaType eval(ITypeEnvironment environment);

	@Override
	public IJavaConcreteType resolve();
}
