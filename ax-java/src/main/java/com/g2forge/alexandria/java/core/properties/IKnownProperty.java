package com.g2forge.alexandria.java.core.properties;

import java.util.Properties;

import com.g2forge.alexandria.java.adt.name.IStringNamed;

public interface IKnownProperty<T> extends IStringNamed {
	public default IPropertyAccessor<T> getAccessor() {
		return getAccessor(System.getProperties());
	}

	public IPropertyAccessor<T> getAccessor(Properties properties);

	public default String getName() {
		final Class<?> type = getClass(), superclass = type.getSuperclass(), enumType;
		if (!Enum.class.equals(superclass) && Enum.class.isAssignableFrom(superclass)) enumType = type.getSuperclass();
		else enumType = type;
		return enumType.getSimpleName().toLowerCase() + "." + name().toLowerCase();
	}

	public String name();
}
