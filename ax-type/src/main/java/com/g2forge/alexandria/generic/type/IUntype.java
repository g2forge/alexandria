package com.g2forge.alexandria.generic.type;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;

public interface IUntype {
	public IUntype eval(ITypeEnvironment environment);

	public ITypeEnvironment toEnvironment();
}
