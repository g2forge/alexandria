package com.g2forge.alexandria.generic.type.java.type.implementations;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import com.g2forge.alexandria.generic.type.IVariableType;
import com.g2forge.alexandria.generic.type.TypeNotConcreteException;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.TypeEnvironment;
import com.g2forge.alexandria.generic.type.java.JavaTypeHelpers;
import com.g2forge.alexandria.generic.type.java.type.AJavaType;
import com.g2forge.alexandria.generic.type.java.type.IJavaClassType;
import com.g2forge.alexandria.generic.type.java.type.IJavaConcreteType;
import com.g2forge.alexandria.generic.type.java.type.IJavaType;
import com.g2forge.alexandria.generic.type.java.type.IJavaVariableType;

public class JavaVariableType extends AJavaType<TypeVariable<?>>implements IJavaVariableType {
	public JavaVariableType(final TypeVariable<?> javaType, final ITypeEnvironment environment) {
		super(javaType, environment);
	}

	@Override
	public IJavaClassType erase() {
		return getUpperBounds().get(0).erase();
	}

	@Override
	public IJavaType eval(final ITypeEnvironment environment) {
		if (environment == null) {
			IJavaType current = this;
			while (true) {
				final IJavaType retVal = (IJavaType) this.environment.apply((IVariableType) current);
				if ((retVal == null) || !(retVal instanceof IVariableType) || current.equals(retVal)) return retVal;
				current = retVal;
			}
		} else return new JavaVariableType(javaType, TypeEnvironment.create(null, this.environment, environment));
	}

	@Override
	public List<? extends IJavaType> getUpperBounds() {
		final Type[] bounds = this.getJavaTypeSimple().getBounds();
		final List<IJavaType> retVal = new ArrayList<>(bounds.length);
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

	@Override
	public String toString() {
		final StringBuilder retVal = new StringBuilder();
		if (javaType.getGenericDeclaration() instanceof Class) retVal.append(((Class<?>) javaType.getGenericDeclaration()).getSimpleName()).append(".");
		retVal.append(javaType.toString()).append(" in ").append(environment);
		return retVal.toString();
	}
}
