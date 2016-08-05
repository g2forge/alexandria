package com.g2forge.alexandria.java.core.math;

public class UnrepresentablePrimitiveException extends RuntimeException {
	private static final long serialVersionUID = 8585354156674442172L;

	public UnrepresentablePrimitiveException() {
		super();
	}

	public UnrepresentablePrimitiveException(String message) {
		super(message);
	}

	public UnrepresentablePrimitiveException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnrepresentablePrimitiveException(Throwable cause) {
		super(cause);
	}
}
