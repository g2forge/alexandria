package com.g2forge.alexandria.reflection.record.v2.reflection;

import java.lang.reflect.Type;

import com.g2forge.alexandria.reflection.object.IJavaFieldReflection;
import com.g2forge.alexandria.reflection.record.v2.IPropertyType;

import lombok.Data;

@Data
class FieldPropertyType implements IPropertyType {
	protected final IJavaFieldReflection<Object, Object> field;

	@Override
	public String getName() {
		return getField().getType().getName();
	}

	@Override
	public Type getType() {
		return field.getType().getJavaType();
	}

	@Override
	public Object getValue(Object object) {
		return getField().getAccessor(object).get0();
	}

	@Override
	public void setValue(Object object, Object value) {
		getField().getAccessor(object).set0(value);
	}
}
