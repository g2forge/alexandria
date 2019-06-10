package com.g2forge.alexandria.reflection.record.v2.reflection;

import com.g2forge.alexandria.metadata.v5.annotation.IJavaAnnotated;
import com.g2forge.alexandria.reflection.object.IJavaFieldReflection;
import com.g2forge.alexandria.reflection.object.IJavaTypeReflection;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
class FieldPropertyType<P> extends APropertyType<P> {
	@Getter
	protected final IJavaFieldReflection<Object, P> field;

	@Override
	protected IJavaAnnotated getAnnotated() {
		return getField();
	}

	@Override
	public String getName() {
		return getField().getType().getName();
	}

	@Override
	public IJavaTypeReflection<P> getType() {
		return field.getFieldType();
	}

	@Override
	public P getValue(Object object) {
		return getField().getAccessor(object).get0();
	}

	@Override
	public void setValue(Object object, P value) {
		getField().getAccessor(object).set0(value);
	}
}
