package com.g2forge.alexandria.generic.type;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;

public interface IType {
	public IType eval(ITypeEnvironment environment);

	public ITypeEnvironment toEnvironment();
}
