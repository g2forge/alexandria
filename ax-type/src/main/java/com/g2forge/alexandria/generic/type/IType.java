package com.g2forge.alexandria.generic.type;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;

public interface IType extends IUntype {
	@Override
	public IType eval(ITypeEnvironment environment);

	/**
	 * @return
	 * @throws TypeNotConcreteException
	 *             This type is not concrete, and cannot be made concrete.
	 */
	public IConcreteType toConcrete();
}
