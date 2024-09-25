package com.g2forge.alexandria.java.core.error;

public class IllegalOperationException extends Error {
	private static final long serialVersionUID = -49635096423947391L;

	public IllegalOperationException() {}

	public IllegalOperationException(String message) {
		super(message);
	}

	public IllegalOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalOperationException(Throwable cause) {
		super(cause);
	}
}
