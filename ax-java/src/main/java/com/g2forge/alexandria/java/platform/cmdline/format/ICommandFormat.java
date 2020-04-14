package com.g2forge.alexandria.java.platform.cmdline.format;

public interface ICommandFormat {
	/**
	 * Quote the specified command argument.
	 * 
	 * @param argument A command line argument that needs to be quoted.
	 * @return A quoted representation of the command line argument. This string must begin and end in double quotes. If it does not, the caller may throw an
	 *         exception as it likely cannot construct a correct command line.
	 */
	public String quote(String argument);
}
