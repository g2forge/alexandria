package com.g2forge.alexandria.generic.type.java;

import java.util.Objects;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;

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
	
	/* @see java.lang.Object#equals(java.lang.Object) */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof AJavaUntype)) return false;
		
		final AJavaUntype<?> that = (AJavaUntype<?>) obj;
		return Objects.equals(javaType, that.javaType);
	}
	
	/* @see java.lang.Object#hashCode() */
	@Override
	public int hashCode() {
		return Objects.hashCode(javaType);
	}
	
	@Override
	public ITypeEnvironment toEnvironment() {
		return EmptyTypeEnvironment.create();
	}
	
	@Override
	public boolean isEnum() {
		return false;
	}
}
