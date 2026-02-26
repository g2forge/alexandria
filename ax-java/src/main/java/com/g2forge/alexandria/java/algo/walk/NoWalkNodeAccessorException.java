package com.g2forge.alexandria.java.algo.walk;

public class NoWalkNodeAccessorException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	protected final Object node;

	protected static String createMessage(Object node, String message) {
		return "No walk node accessor for node \"" + node + "\"" + ((message != null) ? (": " + message) : "");
	}

	public NoWalkNodeAccessorException(Object node, String message) {
		super(createMessage(node, message));
		this.node = node;
	}

	public NoWalkNodeAccessorException(Object node, String message, Throwable cause) {
		super(createMessage(node, message), cause);
		this.node = node;
	}
}
