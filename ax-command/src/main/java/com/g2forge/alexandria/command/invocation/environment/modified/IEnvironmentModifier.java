package com.g2forge.alexandria.command.invocation.environment.modified;

@FunctionalInterface
public interface IEnvironmentModifier {
	/**
	 * Modify the existing environment variable to get the value for the child process.
	 * 
	 * @param value The value of the environment variable in the parent process. The input will be {@code null} if the environment variable was not present in
	 *            the parent process.
	 * @return The value of the environment variable in the child process. If the output is {@code null} the environment variable will not be set for the child.
	 */
	public String modify(String value);
}