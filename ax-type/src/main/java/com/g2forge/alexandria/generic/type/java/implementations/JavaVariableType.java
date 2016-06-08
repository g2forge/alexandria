package com.g2forge.alexandria.generic.type.java.implementations;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;

import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.TypeEnvironment;
import com.g2forge.alexandria.generic.type.java.AJavaType;
import com.g2forge.alexandria.generic.type.java.IJavaClassType;
import com.g2forge.alexandria.generic.type.java.IJavaType;
import com.g2forge.alexandria.generic.type.java.IJavaTypeVariable;
import com.g2forge.alexandria.generic.type.java.JavaTypeHelpers;
import com.g2forge.alexandria.java.CollectionHelpers;

public class JavaVariableType extends AJavaType<TypeVariable<?>>implements IJavaTypeVariable {
	/**
	 * @param javaType
	 * @param environment
	 */
	public JavaVariableType(final TypeVariable<?> javaType, final ITypeEnvironment environment) {
		super(javaType, environment);
	}

	@Override
	public IJavaType eval(final ITypeEnvironment environment) {
		return (IJavaType) TypeEnvironment.create(this.environment, EmptyTypeEnvironment.create(environment)).apply(this);
	}

	@Override
	public IJavaClassType erase() {
		if (environment != null) {
			final IJavaType evaluated = eval(null);
			if (!evaluated.equals(this)) { return evaluated.erase(); }
		}
		return CollectionHelpers.get(getUpperBounds(), 0).erase();
	}

	@Override
	public Collection<? extends IJavaType> getUpperBounds() {
		final Type[] bounds = this.getJavaTypeSimple().getBounds();
		final Collection<IJavaType> retVal = new ArrayList<>(bounds.length);
		for (Type bound : bounds) {
			retVal.add(JavaTypeHelpers.toType(bound, environment));
		}
		return retVal;
	}
}
