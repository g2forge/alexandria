package com.g2forge.alexandria.generic.type.java;

import java.lang.reflect.Type;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;

public abstract class AJavaType<JT extends Type> extends AJavaUntype<JT> implements IJavaType {
	/**
	 * @param javaType
	 * @param environment
	 */
	public AJavaType(final JT javaType, final ITypeEnvironment environment) {
		super(javaType, environment);
	}
	
	@Override
	public Type getJavaType() {
		if (environment != null) {
			final IJavaType evaluated = eval(null);
			if (!evaluated.equals(this)) { return evaluated.getJavaType(); }
		}
		return getJavaTypeSimple();
	}
	
	protected JT getJavaTypeSimple() {
		return javaType;
	}
}
