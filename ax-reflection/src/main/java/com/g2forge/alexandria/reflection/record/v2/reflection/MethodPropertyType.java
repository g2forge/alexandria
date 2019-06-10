package com.g2forge.alexandria.reflection.record.v2.reflection;

import java.lang.reflect.InvocationTargetException;

import com.g2forge.alexandria.java.core.error.RuntimeReflectionException;
import com.g2forge.alexandria.java.reflect.IJavaAccessorMethod;
import com.g2forge.alexandria.metadata.v3.IJavaAnnotated;
import com.g2forge.alexandria.reflection.object.IJavaMethodReflection;
import com.g2forge.alexandria.reflection.object.IJavaTypeReflection;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @see com.g2forge.alexandria.analysis.IMethodAnaluzer which has similar code!
 */
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
class MethodPropertyType<P> extends APropertyType<P> {
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
		return IJavaAccessorMethod.Accessor.getFieldName(getMethod().getType().getName());
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
