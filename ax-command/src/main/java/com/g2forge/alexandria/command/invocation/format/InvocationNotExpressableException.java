package com.g2forge.alexandria.command.invocation.format;

public class InvocationNotExpressableException extends UnsupportedOperationException {
	private static final long serialVersionUID = -7973115468696226395L;

	public InvocationNotExpressableException() {
		super();
	}

	public InvocationNotExpressableException(String message) {
		super(message);
	}

	public InvocationNotExpressableException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvocationNotExpressableException(Throwable cause) {
		super(cause);
	}
}
