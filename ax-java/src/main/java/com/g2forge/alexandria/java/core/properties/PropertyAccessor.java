package com.g2forge.alexandria.java.core.properties;

import java.util.Properties;

import com.g2forge.alexandria.java.function.IFunction1;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class PropertyAccessor<T> implements IPropertyAccessor<T> {
	protected final Properties properties;

	protected final String name;

	protected final T defaultValue;

	protected final IFunction1<? super String, ? extends T> deserializer;

	@Override
	public T get() {
		final String string = getProperties().getProperty(getName());
		if (string == null) return getDefaultValue();
		return getDeserializer().apply(string);
	}
}
