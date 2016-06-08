package com.g2forge.alexandria.generic.java.io;

import java.io.IOException;

public class RuntimeIOException extends RuntimeException {
	private static final long serialVersionUID = 7699108937380712357L;
	
	public RuntimeIOException(final IOException cause) {
		super(cause);
	}
	
	public RuntimeIOException(final String message, final IOException cause) {
		super(message, cause);
	}
}
