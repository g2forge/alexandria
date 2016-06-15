package com.g2forge.alexandria.generic.environment;

import com.g2forge.alexandria.generic.environment.implementations.EmptyEnvironment;

public interface IEnvReference<T, E extends IEnvironment> extends IEnvironmental<E> {
	@Override
	public IEnvReference<T, EmptyEnvironment> bind(E environment);
	
	public T eval(E environment);
}
