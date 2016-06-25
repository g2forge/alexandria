package com.g2forge.alexandria.generic.type;

public class TypeNotConcreteException extends RuntimeException {
	private static final long serialVersionUID = -137213834070032304L;

	public TypeNotConcreteException() {}

	public TypeNotConcreteException(String message) {
		super(message);
	}

	public TypeNotConcreteException(String message, Throwable cause) {
		super(message, cause);
	}

	public TypeNotConcreteException(Throwable cause) {
		super(cause);
	}
}
