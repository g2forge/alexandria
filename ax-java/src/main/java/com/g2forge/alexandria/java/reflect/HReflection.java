package com.g2forge.alexandria.java.reflect;

import java.lang.reflect.Method;

import com.g2forge.alexandria.java.marker.Helpers;

@Helpers
public class HReflection {
	public static String toSignature(Class<?> clazz) {
		if (clazz.isArray()) return clazz.getName();
		return "L" + clazz.getName().replace('.', '/') + ";";
	}

	public static String toSignature(Method method) {
		final StringBuilder retVal = new StringBuilder();
		retVal.append('(');

		final Class<?>[] parameterTypes = method.getParameterTypes();
		for (int i = 0; i < parameterTypes.length; i++) {
			if (i > 0) retVal.append(",");
			retVal.append(toSignature(parameterTypes[i]));
		}

		retVal.append(')').append(toSignature(method.getReturnType()));
		return retVal.toString();
	}
}
