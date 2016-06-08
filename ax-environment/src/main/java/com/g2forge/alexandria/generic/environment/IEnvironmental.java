package com.g2forge.alexandria.generic.environment;

import com.g2forge.alexandria.generic.environment.implementations.EmptyEnvironment;

public interface IEnvironmental<E extends IEnvironment> {
	public IEnvironmental<EmptyEnvironment> bind(E environment);
}
