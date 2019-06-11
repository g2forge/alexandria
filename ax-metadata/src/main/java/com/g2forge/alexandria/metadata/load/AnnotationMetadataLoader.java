package com.g2forge.alexandria.metadata.load;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.metadata.IMetadata;
import com.g2forge.alexandria.metadata.IMetadataLoader;
import com.g2forge.alexandria.metadata.annotation.IJavaAnnotations;
import com.g2forge.alexandria.metadata.annotation.implementations.DynamicAnnotationInvocationHandler;

public class AnnotationMetadataLoader implements IMetadataLoader, ISingleton {
	protected static Class<?> getRepeatable(Class<?> container) {
		final Method[] methods = container.getDeclaredMethods();
		if ((methods.length == 1) && (methods[0].getName().equals("value"))) {
			final Class<?> returnType = methods[0].getReturnType();
			if (returnType.isArray()) {
				final Class<?> componentType = returnType.getComponentType();
				if (componentType.isAnnotation()) {
					final Repeatable repeatable = componentType.getAnnotation(Repeatable.class);
					if (repeatable.value().equals(container)) { return componentType; }
				}
			}
		}
		return null;
	}

	protected void check(Class<?> type) {
		// Note we use the native annotation API here, since this can never be general metadata anyway
		final Retention retention = type.getAnnotation(Retention.class);
		if ((retention == null) || !RetentionPolicy.RUNTIME.equals(retention.value())) throw new IllegalArgumentException("The annotation \"" + type.getName() + "\" cannot be read at runtime, since it is not retained!");
	}

	protected IJavaAnnotations getAnnotations(IMetadata metadata) {
		return ((IAnnotatedMetadata) metadata).getAnnotations();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean isPresent(Class<?> type, IMetadata metadata) {
		check(type);
		return getAnnotations(metadata).isAnnotated((Class) type);
	}

	@Override
	public <T> T load(Class<T> type, IMetadata metadata) {
		check(type);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final T retVal = (T) getAnnotations(metadata).getAnnotation((Class) type);
		if (retVal == null) {
			final Class<?> repeatableType = getRepeatable(type);
			if (repeatableType != null) {
				final Object repeatable = metadata.getMetadata(repeatableType);
				if (repeatable == null) return null;

				final Object array = Array.newInstance(repeatableType, 1);
				Array.set(array, 0, repeatable);

				@SuppressWarnings({ "rawtypes", "unchecked" })
				final T retValDynamic = (T) new DynamicAnnotationInvocationHandler.Builder<Annotation>((Class) type).add("value", array).build();
				return retValDynamic;
			}
		}
		return retVal;
	}
}
