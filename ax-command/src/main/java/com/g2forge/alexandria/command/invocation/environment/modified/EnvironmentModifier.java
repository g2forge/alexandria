package com.g2forge.alexandria.command.invocation.environment.modified;

/**
 * Specify how to generate the value for an environment variable.
 * 
 * @see EnvironmentValue
 */
public enum EnvironmentModifier implements IEnvironmentModifier {
	/** Inherit the environment variable from the parent process. */
	Inherit {
		@Override
		public String modify(String value) {
			return value;
		}
	},
	/** Ensure the variable is not specified to the child process. */
	Unspecified {
		@Override
		public String modify(String value) {
			return null;
		}
	};
}