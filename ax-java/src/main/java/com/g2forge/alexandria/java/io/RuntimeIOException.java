package com.g2forge.alexandria.java.io;

import java.io.IOException;

import com.g2forge.alexandria.java.core.error.IRuntimeWrappingException;

public class RuntimeIOException extends RuntimeException implements IRuntimeWrappingException {
	private static final long serialVersionUID = -2918473990606425790L;

	public RuntimeIOException() {}

	public RuntimeIOException(String message) {
		super(message);
	}

	public RuntimeIOException(String message, IOException cause) {
		super(message, cause);
	}

	public RuntimeIOException(IOException cause) {
		super(cause);
	}

	public IOException getCause() {
		return (IOException) super.getCause();
	}
}
