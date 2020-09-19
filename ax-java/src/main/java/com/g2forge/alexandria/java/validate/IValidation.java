package com.g2forge.alexandria.java.validate;

import com.g2forge.alexandria.java.adt.attributes.IHasValid;

public interface IValidation extends IHasValid {
	public default String getMessage() {
		return getClass().getSimpleName();
	}

	public default void throwIfInvalid() {
		if (!isValid()) throw new ValidationFailureException(this);
	}
}
