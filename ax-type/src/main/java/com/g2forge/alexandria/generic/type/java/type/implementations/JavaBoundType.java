package com.g2forge.alexandria.generic.type.java.type.implementations;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.g2forge.alexandria.generic.type.IType;
import com.g2forge.alexandria.generic.type.IVariableType;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.environment.implementations.TypeEnvironment;
import com.g2forge.alexandria.generic.type.java.IJavaUntype;
import com.g2forge.alexandria.generic.type.java.HJavaType;
import com.g2forge.alexandria.generic.type.java.type.AJavaConcreteType;
import com.g2forge.alexandria.generic.type.java.type.IJavaClassType;
import com.g2forge.alexandria.generic.type.java.type.IJavaConcreteType;
import com.g2forge.alexandria.generic.type.java.type.IJavaType;
import com.g2forge.alexandria.java.core.helpers.HArray;

public class JavaBoundType extends AJavaConcreteType<ParameterizedType> {
	public JavaBoundType(final ParameterizedType javaType, final ITypeEnvironment environment) {
		super(javaType, environment);
	}

	@Override
	public IJavaClassType erase() {
		return getRaw().resolve().erase();
	}

	@Override
	public IJavaConcreteType eval(final ITypeEnvironment environment) {
		return new JavaBoundType(javaType, TypeEnvironment.create(null, this.environment, EmptyTypeEnvironment.create(environment)));
	}

	@Override
	public List<? extends IJavaType> getActuals() {
		return HArray.map(input -> HJavaType.toType(input, environment), javaType.getActualTypeArguments());
	}

	@Override
	public IJavaType getOwner() {
		return HJavaType.toType(javaType.getOwnerType(), environment);
	}

	@Override
	public IJavaType getRaw() {
		return HJavaType.toType(javaType.getRawType(), environment);
	}

	@Override
	public boolean isEnum() {
		return false;
	}

	public ITypeEnvironment toEnvironment() {
		final IJavaUntype owner = getOwner();
		final ITypeEnvironment parent = ((owner != null) && (owner instanceof IJavaConcreteType)) ? ((IJavaConcreteType) owner).toEnvironment() : null;

		if (javaType.getRawType() instanceof GenericDeclaration) {
			final TypeVariable<?>[] parameters = ((GenericDeclaration) javaType.getRawType()).getTypeParameters();
			final Type[] actuals = javaType.getActualTypeArguments();

			final Map<IVariableType, IType> map = new LinkedHashMap<>();
			final ITypeEnvironment retVal = new TypeEnvironment(map, parent);

			for (int i = 0; i < parameters.length; i++) {
				final JavaVariableType parameter = new JavaVariableType(parameters[i], null);
				final IJavaUntype actual = HJavaType.toType(actuals[i], retVal);
				if (!Objects.equals(parameter, actual)) map.put(parameter, actual);
			}

			return retVal;
		}

		return TypeEnvironment.create(parent);
	}

	@Override
	public IJavaConcreteType toNonPrimitive() {
		return this;
	}
}
