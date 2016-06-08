package com.g2forge.alexandria.generic.java.io;

public class IllegalDataException extends IllegalArgumentException {
	private static final long serialVersionUID = -3889991776337547595L;
	
	public IllegalDataException() {}
	
	public IllegalDataException(final String message) {
		super(message);
	}
	
	public IllegalDataException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	public IllegalDataException(final Throwable cause) {
		super(cause);
	}
}
