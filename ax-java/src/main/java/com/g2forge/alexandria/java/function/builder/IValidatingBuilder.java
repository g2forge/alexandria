package com.g2forge.alexandria.java.function.builder;

import com.g2forge.alexandria.java.validate.IValidatable;

public interface IValidatingBuilder<T extends IValidatable> extends IBuilder<T> {
	public default T valid() {
		final T retVal = build();
		retVal.validate().throwIfInvalid();
		return retVal;
	}
}
