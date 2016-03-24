package com.g2forge.alexandria.java.function;

public class UnmappedInputException extends RuntimeException {
	private static final long serialVersionUID = -1457789484285596718L;

	public UnmappedInputException() {}

	public UnmappedInputException(String message) {
		super(message);
	}

	public UnmappedInputException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnmappedInputException(Throwable cause) {
		super(cause);
	}
}
