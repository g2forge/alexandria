package com.g2forge.alexandria.java.validate;

import com.g2forge.alexandria.java.adt.attributes.IHasValid;

public interface IValidatable extends IHasValid {
	@Override
	public default boolean isValid() {
		return validate().isValid();
	}

	public IValidation validate();
}
