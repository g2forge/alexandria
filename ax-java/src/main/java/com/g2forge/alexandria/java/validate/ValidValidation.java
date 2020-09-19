package com.g2forge.alexandria.java.validate;

import com.g2forge.alexandria.java.core.marker.ISingleton;

public class ValidValidation implements IValidation, ISingleton {
	protected static final ValidValidation INSTANCE = new ValidValidation();

	public static ValidValidation create() {
		return INSTANCE;
	}

	protected ValidValidation() {}

	@Override
	public boolean isValid() {
		return true;
	}
}
