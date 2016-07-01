package com.g2forge.alexandria.generic.type.java.type.implementations;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;

import com.g2forge.alexandria.generic.type.TypeNotConcreteException;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.TypeEnvironment;
import com.g2forge.alexandria.generic.type.java.JavaTypeHelpers;
import com.g2forge.alexandria.generic.type.java.type.AJavaType;
import com.g2forge.alexandria.generic.type.java.type.IJavaConcreteType;
import com.g2forge.alexandria.generic.type.java.type.IJavaType;
import com.g2forge.alexandria.generic.type.java.type.IJavaVariableType;

public class JavaVariableType extends AJavaType<TypeVariable<?>>implements IJavaVariableType {
	public JavaVariableType(final TypeVariable<?> javaType, final ITypeEnvironment environment) {
		super(javaType, environment);
	}

	@Override
	public IJavaType eval(final ITypeEnvironment environment) {
		return (IJavaType) TypeEnvironment.create(this.environment, EmptyTypeEnvironment.create(environment)).apply(this);
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

	@Override
	public IJavaConcreteType resolve() {
		final IJavaType eval = eval(null);
		if (eval == this) throw new TypeNotConcreteException();
		return eval.resolve();
	}
}
