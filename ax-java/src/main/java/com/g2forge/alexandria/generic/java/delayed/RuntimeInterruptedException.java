package com.g2forge.alexandria.generic.java.delayed;

public class RuntimeInterruptedException extends RuntimeException {
	private static final long serialVersionUID = -2250012381759988490L;
	
	public RuntimeInterruptedException(final InterruptedException cause) {
		super(cause);
	}
	
	public RuntimeInterruptedException(final String message, final InterruptedException cause) {
		super(message, cause);
	}
}
