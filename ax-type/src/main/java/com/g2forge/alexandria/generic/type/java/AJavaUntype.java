package com.g2forge.alexandria.generic.type.java;

import java.util.Objects;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;

public abstract class AJavaUntype<JT> implements IJavaUntype {
	protected final JT javaType;

	protected final ITypeEnvironment environment;

	/**
	 * @param javaType
	 * @param environment
	 */
	public AJavaUntype(final JT javaType, final ITypeEnvironment environment) {
		this.javaType = javaType;
		this.environment = environment;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof AJavaUntype)) return false;

		final AJavaUntype<?> that = (AJavaUntype<?>) obj;
		return Objects.equals(javaType, that.javaType);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(javaType);
	}

	@Override
	public String toString() {
		return javaType.toString() + " in " + environment;
	}
}
