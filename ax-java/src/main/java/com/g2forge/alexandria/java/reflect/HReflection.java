package com.g2forge.alexandria.java.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.g2forge.alexandria.java.core.error.ReflectedCodeError;
import com.g2forge.alexandria.java.core.marker.Helpers;

@Helpers
public class HReflection {
	protected static void addSignature(StringBuilder builder, Executable executable) {
		builder.append('(');

		final Class<?>[] parameterTypes = executable.getParameterTypes();
		for (int i = 0; i < parameterTypes.length; i++) {
			builder.append(toSignature(parameterTypes[i]));
		}

		builder.append(')');
	}

	@SuppressWarnings("unchecked")
	public static <T> T getDefaultValue(Class<T> type) {
		return (T) Array.get(Array.newInstance(type, 1), 0);
	}

	public static Type getParentTypeArgument(Object child, Class<?> expected, int index) {
		if (expected.getTypeParameters().length < index) throw new IllegalArgumentException(String.format("Cannot get type argument %1$d of type \"%2$s\", because it has only %3$d parameters!", index, expected.getClass(), expected.getTypeParameters().length));

		// Get the actual class
		final Class<? extends Object> klass = child.getClass();
		for (int i = -1; i < klass.getGenericInterfaces().length; i++) {
			// Get it's superclass, which should be the expected type
			final Type parent = (i == -1) ? klass.getGenericSuperclass() : klass.getGenericInterfaces()[i];
			// Check that our expectations are being met
			if (!(parent instanceof ParameterizedType)) continue;
			final ParameterizedType parameterized = (ParameterizedType) parent;
			if (!parameterized.getRawType().equals(expected)) continue;

			// Get the dynamic type
			return parameterized.getActualTypeArguments()[index];
		}

		throw new ReflectedCodeError("Could not find appropriately parameterized parent type!");
	}

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
		if (clazz.isArray()) return clazz.getName().replace('.', '/');
		return "L" + clazz.getName().replace('.', '/') + ";";
	}

	public static String toSignature(Constructor<?> constructor) {
		final StringBuilder retVal = new StringBuilder();
		addSignature(retVal, constructor);
		retVal.append(toSignature(Void.TYPE));
		return retVal.toString();
	}

	public static String toSignature(Method method) {
		final StringBuilder retVal = new StringBuilder();
		addSignature(retVal, method);
		retVal.append(toSignature(method.getReturnType()));
		return retVal.toString();
	}
}
