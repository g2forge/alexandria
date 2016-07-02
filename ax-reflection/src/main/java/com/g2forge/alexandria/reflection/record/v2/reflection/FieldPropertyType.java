package com.g2forge.alexandria.reflection.record.v2.reflection;

import java.lang.reflect.Type;

import com.g2forge.alexandria.reflection.annotations.IJavaAnnotated;
import com.g2forge.alexandria.reflection.object.IJavaFieldReflection;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
class FieldPropertyType extends APropertyType {
	@Getter
	protected final IJavaFieldReflection<Object, Object> field;

	@Override
	protected IJavaAnnotated getAnnotated() {
		return getField();
	}

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
