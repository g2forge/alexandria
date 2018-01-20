package com.g2forge.alexandria.generic.type;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;

public interface IResolvableType extends IType {
	@Override
	public IResolvableType eval(ITypeEnvironment environment);

	/**
	 * Resolve this type into a concrete one.
	 * 
	 * @return This type as a concrete type.
	 * @throws TypeNotConcreteException This type is not concrete, and cannot be made concrete.
	 */
	public IConcreteType resolve();
}
