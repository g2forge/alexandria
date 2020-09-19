package com.g2forge.alexandria.java.validate;

public class ValidationFailureException extends RuntimeException {
	private static final long serialVersionUID = -9142186418030436823L;

	protected final IValidation validation;

	public ValidationFailureException(IValidation validation) {
		super(validation.getMessage());
		this.validation = validation;
	}
}
