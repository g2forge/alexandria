package com.g2forge.alexandria.java;

public class UnreachableCodeError extends Error {
	private static final long serialVersionUID = -6223821547134401350L;
	
	public UnreachableCodeError() {}
	
	public UnreachableCodeError(final String message) {
		super(message);
	}
	
	public UnreachableCodeError(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	public UnreachableCodeError(final Throwable cause) {
		super(cause);
	}
}
