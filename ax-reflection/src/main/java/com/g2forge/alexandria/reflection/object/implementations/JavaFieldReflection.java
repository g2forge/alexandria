package com.g2forge.alexandria.reflection.object.implementations;

import java.lang.reflect.Field;

import com.g2forge.alexandria.generic.type.java.member.IJavaFieldType;
import com.g2forge.alexandria.generic.type.java.type.implementations.ReflectionException;
import com.g2forge.alexandria.java.adt.tuple.ITuple1GS;
import com.g2forge.alexandria.reflection.object.AJavaMemberReflection;
import com.g2forge.alexandria.reflection.object.HReflection;
import com.g2forge.alexandria.reflection.object.IJavaFieldReflection;
import com.g2forge.alexandria.reflection.object.IJavaTypeReflection;

public class JavaFieldReflection<T, F> extends AJavaMemberReflection<T, IJavaFieldType>implements IJavaFieldReflection<T, F> {
	public JavaFieldReflection(IJavaFieldType type) {
		super(type);
	}

	@Override
	public ITuple1GS<F> getAccessor(final T object) {
		final Field field = getType().getJavaMember();
		field.setAccessible(true);
		return new ITuple1GS<F>() {
			@Override
			public F get0() {
				try {
					@SuppressWarnings("unchecked")
					final F retVal = (F) field.get(object);
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
		};
	}

	@Override
	public IJavaTypeReflection<F> getFieldType() {
		return HReflection.toReflection(getType().getJavaMember().getGenericType());
	}
}
