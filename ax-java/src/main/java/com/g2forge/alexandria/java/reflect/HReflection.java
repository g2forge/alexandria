package com.g2forge.alexandria.java.reflect;

import java.lang.reflect.Method;

import com.g2forge.alexandria.java.marker.Helpers;

@Helpers
public class HReflection {
	public static String toSignature(Class<?> clazz) {
		if (clazz.isPrimitive()) {
			if (Void.class.equals(clazz) || Void.TYPE.equals(clazz)) return "V";
			if (Boolean.class.equals(clazz) || Boolean.TYPE.equals(clazz)) return "Z";
			if (Character.class.equals(clazz) || Character.TYPE.equals(clazz)) return "C";
			if (Byte.class.equals(clazz) || Byte.TYPE.equals(clazz)) return "B";
			if (Short.class.equals(clazz) || Short.TYPE.equals(clazz)) return "S";
			if (Integer.class.equals(clazz) || Integer.TYPE.equals(clazz)) return "I";
			if (Long.class.equals(clazz) || Long.TYPE.equals(clazz)) return "J";
			if (Float.class.equals(clazz) || Float.TYPE.equals(clazz)) return "F";
			if (Double.class.equals(clazz) || Double.TYPE.equals(clazz)) return "D";
			throw new IllegalArgumentException();
		}
		if (clazz.isArray()) return clazz.getName();
		return "L" + clazz.getName().replace('.', '/') + ";";
	}

	public static String toSignature(Method method) {
		final StringBuilder retVal = new StringBuilder();
		retVal.append('(');

		final Class<?>[] parameterTypes = method.getParameterTypes();
		for (int i = 0; i < parameterTypes.length; i++) {
			retVal.append(toSignature(parameterTypes[i]));
		}

		retVal.append(')').append(toSignature(method.getReturnType()));
		return retVal.toString();
	}
}
