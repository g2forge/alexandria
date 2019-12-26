package com.g2forge.alexandria.java.reflect.accessor;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JavaAccessorMethod implements IJavaAccessorMethod {
	protected final Method method;

	@Override
	public String getName() {
		return getMethod().getName();
	}

	@Override
	public Type[] getParameterTypes() {
		return getMethod().getGenericParameterTypes();
	}

	@Override
	public Type getReturnType() {
		return getMethod().getGenericReturnType();
	}
}
