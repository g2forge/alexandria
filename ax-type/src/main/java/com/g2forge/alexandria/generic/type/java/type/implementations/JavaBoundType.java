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
import com.g2forge.alexandria.generic.type.java.JavaTypeHelpers;
import com.g2forge.alexandria.generic.type.java.type.AJavaConcreteType;
import com.g2forge.alexandria.generic.type.java.type.IJavaBoundType;
import com.g2forge.alexandria.generic.type.java.type.IJavaClassType;
import com.g2forge.alexandria.generic.type.java.type.IJavaType;
import com.g2forge.alexandria.java.core.helpers.ArrayHelpers;

public class JavaBoundType extends AJavaConcreteType<ParameterizedType>implements IJavaBoundType {
	public JavaBoundType(final ParameterizedType javaType, final ITypeEnvironment environment) {
		super(javaType, environment);
	}

	@Override
	public IJavaClassType erase() {
		return getRaw().resolve().erase();
	}

	@Override
	public IJavaBoundType eval(final ITypeEnvironment environment) {
		return new JavaBoundType(javaType, TypeEnvironment.create(this.environment, EmptyTypeEnvironment.create(environment)));
	}

	@Override
	public List<? extends IJavaType> getActuals() {
		return ArrayHelpers.map(input -> JavaTypeHelpers.toType(input, environment), javaType.getActualTypeArguments());
	}

	@Override
	public IJavaType getOwner() {
		return JavaTypeHelpers.toType(javaType.getOwnerType(), environment);
	}

	@Override
	public IJavaType getRaw() {
		return JavaTypeHelpers.toType(javaType.getRawType(), environment);
	}

	@Override
	public boolean isEnum() {
		return false;
	}

	@Override
	public ITypeEnvironment toEnvironment() {
		final IJavaUntype owner = getOwner();
		final ITypeEnvironment parent = owner.toEnvironment();

		if (javaType.getRawType() instanceof GenericDeclaration) {
			final TypeVariable<?>[] parameters = ((GenericDeclaration) javaType.getRawType()).getTypeParameters();
			final Type[] actuals = javaType.getActualTypeArguments();

			final Map<IVariableType, IType> map = new LinkedHashMap<>();
			final ITypeEnvironment retVal = new TypeEnvironment(map, parent);

			for (int i = 0; i < parameters.length; i++) {
				final JavaVariableType parameter = new JavaVariableType(parameters[i], retVal);
				final IJavaUntype actual = JavaTypeHelpers.toType(actuals[i], retVal);
				if (!Objects.equals(parameter, actual)) map.put(parameter, actual);
			}

			return retVal;
		}

		return TypeEnvironment.create(parent);
	}
}
