package com.g2forge.alexandria.generic.type.java.type;

import java.lang.reflect.Type;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.AJavaUntype;
import com.g2forge.alexandria.generic.type.java.JavaTypeHelpers;
import com.g2forge.alexandria.generic.type.java.type.implementations.JavaClassType;

public abstract class AJavaType<JT extends Type> extends AJavaUntype<JT>implements IJavaType {
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

	protected IJavaClassType getParent(final Type generic, final Class<?> parent) {
		if (generic == null) return null;
		final ITypeEnvironment environment = (this.environment != null) ? JavaTypeHelpers.toType(generic, this.environment).toEnvironment() : null;
		return new JavaClassType(parent, environment);
	}
}
