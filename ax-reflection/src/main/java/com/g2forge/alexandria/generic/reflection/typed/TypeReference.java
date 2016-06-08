package com.g2forge.alexandria.generic.reflection.typed;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.g2forge.alexandria.generic.reflection.object.IJavaTypeReflection;
import com.g2forge.alexandria.generic.reflection.object.ReflectionHelpers;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;

public abstract class TypeReference<T> implements IReflectionGenericTyped<T> {
	protected final IJavaTypeReflection<T> type;
	
	protected TypeReference() {
		final Type superClass = getClass().getGenericSuperclass();
		if (!(superClass instanceof ParameterizedType)) throw new IllegalArgumentException("Type reference constructed without any concrete type!");
		final ParameterizedType parameterizedType = (ParameterizedType) superClass;
		if (parameterizedType.getActualTypeArguments().length != 1) throw new IllegalArgumentException("Type reference constructed with the wrong number of type arguments!");
		type = ReflectionHelpers.toReflection(parameterizedType.getActualTypeArguments()[0], EmptyTypeEnvironment.create());
	}
	
	@Override
	public IJavaTypeReflection<T> getType() {
		return type;
	}
}
