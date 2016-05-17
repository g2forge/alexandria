package com.g2forge.alexandria.record.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.StringHelpers;
import com.g2forge.alexandria.java.reflection.RuntimeReflectionException;
import com.g2forge.alexandria.record.IPropertyType;

import lombok.Data;

@Data
class GetterPropertyType implements IPropertyType {
	public enum GetterType {
		GET {
			@Override
			public boolean isMatchingReturnType(Type type) {
				return !Void.TYPE.equals(type);
			}
		},
		IS {
			@Override
			public boolean isMatchingReturnType(Type type) {
				return Boolean.TYPE.equals(type) || Boolean.class.equals(type);
			}
		};

		public String getPrefix() {
			return name().toLowerCase();
		}

		public abstract boolean isMatchingReturnType(Type type);
	}

	protected static final String[] PREFIXES = Stream.of(GetterType.values()).map(GetterType::getPrefix).collect(Collectors.toList()).toArray(new String[0]);

	public static boolean isGetter(Method method) {
		if (method.getParameterCount() != 0) return false;

		final String name = method.getName();
		final Type genericReturnType = method.getGenericReturnType();
		for (GetterType getterType : GetterType.values()) {
			if (name.startsWith(getterType.getPrefix()) && getterType.isMatchingReturnType(genericReturnType)) return true;
		}
		return false;
	}

	protected final Method getter;

	public GetterPropertyType(Method getter) {
		this.getter = getter;
		getGetter().setAccessible(true);
	}

	@Override
	public String getName() {
		return StringHelpers.lowercase(StringHelpers.stripPrefix(getGetter().getName(), PREFIXES));
	}

	@Override
	public Object getValue(Object object) {
		try {
			return getGetter().invoke(object);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
			throw new RuntimeReflectionException(exception);
		}
	}

	@Override
	public void setValue(Object object, Object value) {
		throw new UnsupportedOperationException();
	}
}
