package com.g2forge.alexandria.java.core.properties;

import java.util.Properties;

public interface IKnownPropertyBoolean extends IKnownProperty<Boolean> {
	@Override
	public default IPropertyAccessor<Boolean> getAccessor(Properties properties) {
		return new PropertyAccessor<Boolean>(properties, getName(), getDefault(), Boolean::parseBoolean);
	}

	public default Boolean getDefault() {
		return false;
	}
}
