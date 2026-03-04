package com.g2forge.alexandria.command.invocation;

public interface ICommandArgumentType<A> {
	public String get(A argument);

	public A create(String string);

	/**
	 * Create a new argument based on an original argument, but with the specified string value. This is useful when the argument type has metadata and the new
	 * argument will be derived somehow from the original one.
	 * 
	 * @param string The string value of the new argument.
	 * @param original The original argument this one should be considered to be derived from.
	 * @return A new argument.
	 */
	public A create(String string, A original);
}
