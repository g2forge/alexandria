package com.g2forge.alexandria.reflection.record.v2.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.core.error.RuntimeReflectionException;
import com.g2forge.alexandria.java.core.helpers.StringHelpers;
import com.g2forge.alexandria.reflection.annotations.IJavaAnnotated;
import com.g2forge.alexandria.reflection.object.IJavaMethodReflection;
import com.g2forge.alexandria.reflection.object.IJavaTypeReflection;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
class MethodPropertyType<P> extends APropertyType<P> {
	public enum MethodType {
		GET {
			@Override
			public boolean isMatchingParameterTypes(Type[] types) {
				return types.length == 0;
			}

			@Override
			public boolean isMatchingReturnType(Type type) {
				return !Void.TYPE.equals(type);
			}
		},
		IS {
			@Override
			public boolean isMatchingParameterTypes(Type[] types) {
				return types.length == 0;
			}

			@Override
			public boolean isMatchingReturnType(Type type) {
				return Boolean.TYPE.equals(type) || Boolean.class.equals(type);
			}
		},
		SET {
			@Override
			public boolean isMatchingParameterTypes(Type[] types) {
				return types.length == 1;
			}

			@Override
			public boolean isMatchingReturnType(Type type) {
				return true;
			}
		};

		public String getPrefix() {
			return name().toLowerCase();
		}

		public abstract boolean isMatchingParameterTypes(Type[] types);

		public abstract boolean isMatchingReturnType(Type type);
	}

	protected static final String[] PREFIXES = Stream.of(MethodType.values()).map(MethodType::getPrefix).collect(Collectors.toList()).toArray(new String[0]);

	public static boolean isAccessor(IJavaMethodReflection<?, ?> reflection) {
		final Method method = reflection.getType().getJavaMember();

		final String name = method.getName();
		final Type[] parameterTypes = method.getGenericParameterTypes();
		final Type returnType = method.getGenericReturnType();
		for (MethodType methodType : MethodType.values()) {
			if (name.startsWith(methodType.getPrefix()) && methodType.isMatchingReturnType(returnType) && methodType.isMatchingParameterTypes(parameterTypes)) return true;
		}
		return false;
	}

	@Getter
	protected APropertyType<P> previous;

	@Getter
	protected final IJavaMethodReflection<?, P> method;

	@Override
	protected IJavaAnnotated getAnnotated() {
		return getMethod();
	}

	@Override
	public String getName() {
		return StringHelpers.lowercase(StringHelpers.stripPrefix(getMethod().getType().getName(), PREFIXES));
	}

	@Override
	public IJavaTypeReflection<P> getType() {
		if (!getMethod().getType().getParameterTypes().isEmpty()) return previous.getType();
		return getMethod().getReturnType();
	}

	@Override
	public P getValue(Object object) {
		if (!getMethod().getType().getParameterTypes().isEmpty()) {
			if (previous != null) return previous.getValue(object);
			throw new UnsupportedOperationException();
		}

		@SuppressWarnings("unchecked")
		final IJavaMethodReflection<Object, P> method = (IJavaMethodReflection<Object, P>) getMethod();
		return method.invoke(object);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void setOverride(APropertyType<?> previous) {
		super.setOverride(previous);
		this.previous = (APropertyType<P>) previous;
	}

	@Override
	public void setValue(Object object, P value) {
		if (getMethod().getType().getParameterTypes().isEmpty()) {
			if (previous != null) {
				previous.setValue(object, value);
				return;
			}
			throw new UnsupportedOperationException();
		}
		try {
			getMethod().getType().getJavaMember().invoke(object, value);
		} catch (IllegalAccessException | InvocationTargetException exception) {
			throw new RuntimeReflectionException(exception);
		}
	}
}
