package com.g2forge.alexandria.java.reflection;

import com.g2forge.alexandria.java.IRuntimeWrappingException;

public class RuntimeReflectionException extends RuntimeException implements IRuntimeWrappingException {
	private static final long serialVersionUID = 7655660719517593551L;

	public RuntimeReflectionException() {}

	public RuntimeReflectionException(String message) {
		super(message);
	}

	public RuntimeReflectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public RuntimeReflectionException(Throwable cause) {
		super(cause);
	}
}
