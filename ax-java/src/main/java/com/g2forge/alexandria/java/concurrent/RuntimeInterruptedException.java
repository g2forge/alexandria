package com.g2forge.alexandria.java.concurrent;

import com.g2forge.alexandria.java.IRuntimeWrappingException;

public class RuntimeInterruptedException extends RuntimeException implements IRuntimeWrappingException {
	private static final long serialVersionUID = -2250012381759988490L;

	public RuntimeInterruptedException(final InterruptedException cause) {
		super(cause);
	}

	public RuntimeInterruptedException(final String message, final InterruptedException cause) {
		super(message, cause);
	}
}
