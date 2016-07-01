package com.g2forge.alexandria.generic.type;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;

public interface IConcreteType extends IType {
	@Override
	public IConcreteType eval(ITypeEnvironment environment);
}
