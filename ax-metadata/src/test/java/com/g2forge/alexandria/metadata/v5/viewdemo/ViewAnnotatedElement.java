package com.g2forge.alexandria.metadata.v5.viewdemo;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.g2forge.alexandria.adt.associative.cache.Cache;
import com.g2forge.alexandria.adt.associative.cache.NeverCacheEvictionPolicy;
import com.g2forge.alexandria.java.core.error.RuntimeReflectionException;

public class ViewAnnotatedElement extends AFilteredAnnotatedElement {
	protected static final Cache<Class<? extends Annotation>, Method> cache = new Cache<>(ViewAnnotatedElement::getViewMethod, NeverCacheEvictionPolicy.create());

	protected static Method getViewMethod(Class<? extends Annotation> type) {
		for (Method method : type.getMethods()) {
			final ViewAnnotation viewAnnotation = method.getAnnotation(ViewAnnotation.class);
			if (viewAnnotation != null) return method;
		}
		return null;
	}

	protected final Class<?> view;

	public ViewAnnotatedElement(AnnotatedElement element, Class<?> view) {
		super(element);
		this.view = view;
	}

	@Override
	protected boolean filter(Annotation annotation) {
		final Method method = getViewMethod(annotation.annotationType());
		if (method != null) {
			final Class<?> view;
			try {
				view = (Class<?>) method.invoke(annotation);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeReflectionException("Could not get view from annotation!", e);
			}
			return view.isAssignableFrom(this.view);
		}
		return true;
	}
}
