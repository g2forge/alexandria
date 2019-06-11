package com.g2forge.alexandria.java.core.error;

public class RuntimeWrappingException extends RuntimeException implements IRuntimeWrappingException {
	private static final long serialVersionUID = 5154203547852963716L;

	public RuntimeWrappingException() {}

	public RuntimeWrappingException(String message) {
		super(message);
	}

	public RuntimeWrappingException(String message, Throwable cause) {
		super(message, cause);
	}

	public RuntimeWrappingException(Throwable cause) {
		super(cause);
	}
}
