package com.g2forge.alexandria.record.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import com.g2forge.alexandria.java.reflection.RuntimeReflectionException;
import com.g2forge.alexandria.record.IPropertyType;

import lombok.Data;

@Data
class FieldPropertyType implements IPropertyType {
	protected final Field field;

	public FieldPropertyType(Field field) {
		this.field = field;
		getField().setAccessible(true);
	}

	@Override
	public String getName() {
		return getField().getName();
	}

	@Override
	public Type getType() {
		return field.getGenericType();
	}

	@Override
	public Object getValue(Object object) {
		try {
			return getField().get(object);
		} catch (IllegalArgumentException | IllegalAccessException exception) {
			throw new RuntimeReflectionException(exception);
		}
	}

	@Override
	public void setValue(Object object, Object value) {
		try {
			getField().set(object, value);
		} catch (IllegalArgumentException | IllegalAccessException exception) {
			throw new RuntimeReflectionException(exception);
		}
	}
}
