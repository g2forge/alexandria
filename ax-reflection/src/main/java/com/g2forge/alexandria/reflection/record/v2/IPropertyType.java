package com.g2forge.alexandria.reflection.record.v2;

import java.lang.reflect.Type;

import com.g2forge.alexandria.reflection.annotations.IJavaAnnotations;

public interface IPropertyType {
	public IJavaAnnotations getAnnotations();

	public String getName();

	public Type getType();

	public Object getValue(Object object);

	public void setValue(Object object, Object value);
}
