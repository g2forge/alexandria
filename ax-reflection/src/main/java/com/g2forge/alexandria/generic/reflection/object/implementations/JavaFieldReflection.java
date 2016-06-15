package com.g2forge.alexandria.generic.reflection.object.implementations;

import java.lang.reflect.Field;

import com.g2forge.alexandria.generic.reflection.annotations.IJavaAnnotations;
import com.g2forge.alexandria.generic.reflection.annotations.implementations.JavaAnnotations;
import com.g2forge.alexandria.generic.reflection.object.IJavaFieldReflection;
import com.g2forge.alexandria.generic.type.java.IJavaFieldType;
import com.g2forge.alexandria.generic.type.java.implementations.ReflectionException;
import com.g2forge.alexandria.java.tuple.ITuple1GS;

public class JavaFieldReflection<O, F> implements IJavaFieldReflection<O, F> {
	protected final IJavaFieldType type;
	
	/**
	 * @param type
	 */
	public JavaFieldReflection(IJavaFieldType type) {
		this.type = type;
	}
	
	@Override
	public ITuple1GS<F> getAccessor(final O object) {
		final Field field = getInternal();
		field.setAccessible(true);
		return new ITuple1GS<F>() {
			@Override
			public F get0() {
				try {
					@SuppressWarnings("unchecked") final F retVal = (F) field.get(object);
					return retVal;
				} catch (IllegalArgumentException | IllegalAccessException exception) {
					throw new ReflectionException(exception);
				}
			}
			
			@Override
			public ITuple1GS<F> set0(F value) {
				try {
					field.set(object, value);
				} catch (IllegalArgumentException | IllegalAccessException exception) {
					throw new ReflectionException(exception);
				}
				return this;
			}
			
			@Override
			public F swap0(F value) {
				final F retVal = get0();
				set0(value);
				return retVal;
			}
		};
	}

	@Override
	public IJavaAnnotations getAnnotations() {
		return new JavaAnnotations(getInternal());
	}
	
	protected Field getInternal() {
		return getType().getJavaField();
	}

	@Override
	public IJavaFieldType getType() {
		return type;
	}
}
