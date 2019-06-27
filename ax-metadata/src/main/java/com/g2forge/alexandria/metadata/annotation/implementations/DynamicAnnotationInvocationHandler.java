package com.g2forge.alexandria.metadata.annotation.implementations;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.g2forge.alexandria.annotations.note.Note;
import com.g2forge.alexandria.annotations.note.NoteType;
import com.g2forge.alexandria.java.core.error.RuntimeReflectionException;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
public class DynamicAnnotationInvocationHandler implements InvocationHandler {
	@RequiredArgsConstructor
	public static class Builder<T extends Annotation> {
		protected final Class<T> annotationType;

		protected final Map<String, Object> values = new HashMap<>();

		public Builder<T> add(Method method, Object value) {
			if (values.containsKey(method.getName())) throw new IllegalStateException("Value for " + method.getName() + " was already set");
			values.put(method.getName(), value);
			return this;
		}

		public Builder<T> add(String name, Object value) {
			if (values.containsKey(name)) throw new IllegalStateException("Value for " + name + " was already set");
			try {
				annotationType.getMethod(name);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new RuntimeReflectionException("No method with name " + name + " on " + annotationType, e);
			}
			values.put(name, value);
			return this;
		}

		@SuppressWarnings("unchecked")
		public T build() {
			return (T) Proxy.newProxyInstance(annotationType.getClassLoader(), new Class[] { annotationType }, new DynamicAnnotationInvocationHandler(annotationType, values));
		}
	}

	@Getter(value = AccessLevel.PROTECTED, lazy = true)
	private static final Method annotationTypeMethod = computeAnnotationType();

	protected static Method computeAnnotationType() {
		try {
			return java.lang.annotation.Annotation.class.getMethod("annotationType");
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeReflectionException(e);
		}
	}

	protected final Class<?> annotationType;

	protected final Map<String, ?> values;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	protected transient Integer hashCode = null;

	protected int getHashCode() {
		if (hashCode == null) {
			int retVal = 0;

			for (Method method : getAnnotationType().getDeclaredMethods()) {
				final Object value = getValue(method);
				final int valueHash;
				if (value == null) valueHash = 0;
				else if (value.getClass().isArray()) valueHash = Arrays.hashCode((Object[]) value);
				else valueHash = value.hashCode();
				retVal += 127 * method.getName().hashCode() ^ valueHash;
			}

			hashCode = retVal;
			return retVal;
		}
		return hashCode;
	}

	@Note(type = NoteType.TODO, value = "Support actual values")
	protected Object getValue(Method method) {
		final Map<String, ?> values = getValues();
		final String key = method.getName();
		final Object value;
		return (((value = values.get(key)) != null) || values.containsKey(key)) ? value : method.getDefaultValue();
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		final String name = method.getName();
		if (method.getDeclaringClass() == Object.class) {
			if ("equals".equals(name)) return isEquals(args[0]);
			else if ("hashCode".equals(name)) return getHashCode();
			else if ("toString".equals(name)) return toString();
		} else if ("annotationType".equals(name) && (method.getParameterTypes().length == 0)) return getAnnotationType();
		else if (getAnnotationType().equals(method.getDeclaringClass())) return getValue(method);

		throw new UnsupportedOperationException("Does not support method: " + method);
	}

	protected boolean isEquals(Object that) throws Exception {
		if (that == null) return false;

		// Optimization when the other is one of ourselves
		if (that instanceof Proxy) {
			final InvocationHandler thatInvocationHandler = Proxy.getInvocationHandler(that);
			if (thatInvocationHandler instanceof DynamicAnnotationInvocationHandler) return equals(thatInvocationHandler);
		}

		@SuppressWarnings("rawtypes")
		final Class<?> thatAnnotationType = (Class) getAnnotationTypeMethod().invoke(that);
		if (getAnnotationType().equals(thatAnnotationType) == false) return false;

		for (Method method : annotationType.getDeclaredMethods()) {
			final Object thisValue = getValue(method);
			final Object thatValue = method.invoke(that);
			if (!Objects.equals(thisValue, thatValue)) return false;
		}
		return true;
	}
}
