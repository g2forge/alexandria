package com.g2forge.alexandria.generic.type.environment.implementations;

import com.g2forge.alexandria.generic.type.IType;
import com.g2forge.alexandria.generic.type.IVariableType;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.java.core.marker.ISingleton;

public final class EmptyTypeEnvironment implements ITypeEnvironment, ISingleton {
	protected static final EmptyTypeEnvironment singleton = new EmptyTypeEnvironment();

	public static EmptyTypeEnvironment create() {
		return singleton;
	}

	public static ITypeEnvironment create(final ITypeEnvironment parent) {
		if (parent == null) return EmptyTypeEnvironment.create();
		return parent;
	}

	private EmptyTypeEnvironment() {}

	@Override
	public IType apply(final IVariableType input) {
		return input;
	}

	@Override
	public String toString() {
		return "{}";
	}
}
