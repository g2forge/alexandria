package com.g2forge.alexandria.record;

import java.lang.reflect.Type;

public interface IPropertyType {
	public String getName();

	public Type getType();

	public Object getValue(Object object);

	public void setValue(Object object, Object value);
}
