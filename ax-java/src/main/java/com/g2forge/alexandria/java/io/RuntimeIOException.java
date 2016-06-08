package com.g2forge.alexandria.java.io;

import com.g2forge.alexandria.java.core.error.IRuntimeWrappingException;

public class RuntimeIOException extends RuntimeException implements IRuntimeWrappingException {
	private static final long serialVersionUID = -2918473990606425790L;

	public RuntimeIOException() {}

	public RuntimeIOException(String message) {
		super(message);
	}

	public RuntimeIOException(String message, Throwable cause) {
		super(message, cause);
	}

	public RuntimeIOException(Throwable cause) {
		super(cause);
	}
}
