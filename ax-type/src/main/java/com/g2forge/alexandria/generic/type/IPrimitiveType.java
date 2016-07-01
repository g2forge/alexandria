package com.g2forge.alexandria.generic.type;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;

public interface IPrimitiveType extends IResolvableType, IConcreteType {
	@Override
	public IPrimitiveType eval(ITypeEnvironment environment);
}
