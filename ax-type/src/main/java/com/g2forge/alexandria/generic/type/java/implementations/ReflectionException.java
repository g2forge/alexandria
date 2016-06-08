package com.g2forge.alexandria.generic.type.java.implementations;

public class ReflectionException extends RuntimeException {
	private static final long serialVersionUID = -7775511988672940108L;
	
	public ReflectionException() {}
	
	public ReflectionException(final String message) {
		super(message);
	}
	
	public ReflectionException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	public ReflectionException(final Throwable cause) {
		super(cause);
	}
}
