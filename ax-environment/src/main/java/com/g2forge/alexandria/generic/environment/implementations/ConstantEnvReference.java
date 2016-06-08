package com.g2forge.alexandria.generic.environment.implementations;

import com.g2forge.alexandria.generic.environment.IEnvReference;
import com.g2forge.alexandria.generic.environment.IEnvironment;

public class ConstantEnvReference<T, E extends IEnvironment> implements IEnvReference<T, E> {
	public static <T> ConstantEnvReference<T, EmptyEnvironment> create(final T value) {
		return new ConstantEnvReference<>(value);
	}
	
	protected final T value;
	
	/**
	 * @param value
	 */
	public ConstantEnvReference(final T value) {
		this.value = value;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public IEnvReference<T, EmptyEnvironment> bind(final E environment) {
		return (IEnvReference<T, EmptyEnvironment>) this;
	}
	
	@Override
	public T eval(final E context) {
		return value;
	}
}
