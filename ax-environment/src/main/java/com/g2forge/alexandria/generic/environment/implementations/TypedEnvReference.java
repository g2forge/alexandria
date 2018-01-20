package com.g2forge.alexandria.generic.environment.implementations;

import com.g2forge.alexandria.generic.environment.IEnvReference;
import com.g2forge.alexandria.generic.environment.ITypedMapEnvironment;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TypedEnvReference<T> implements IEnvReference<T, ITypedMapEnvironment> {
	protected final Class<T> type;
	
	@Override
	public IEnvReference<T, EmptyEnvironment> bind(final ITypedMapEnvironment environment) {
		return new ConstantEnvReference<>(eval(environment));
	}
	
	@Override
	public T eval(final ITypedMapEnvironment environment) {
		return environment.apply(type);
	}
	
}
