package com.g2forge.alexandria.java.platform.cmdline.format;

/**
 * A per-command interface to describe how arguments to this command should be quoted by {@link ICommandLineBuilder}. This is per-command because while under
 * POSIX processes are given a pre-parsed command block, but the
 * <a href="https://docs.microsoft.com/en-us/cpp/cpp/main-function-command-line-args?redirectedfrom=MSDN&view=vs-2019">Win32 API</a> specifies that processes
 * get a command line which they are at their leisure to parse any way they like.
 * 
 * If you don't know what command format to use we suggest {@link BashCommandFormat} as most commands, even on Win32, use that format due to the Microsoft CRT0
 * code adopting it. If the command in question is an older one from the DOS era, perhaps {@code cmd.exe} then you may need to use
 * {@link NotExpressableCommandFormat}.
 */
public interface ICommandFormat {
	/**
	 * Quote the specified command argument.
	 * 
	 * @param argument A command line argument that needs to be quoted.
	 * @return A quoted representation of the command line argument. This string must begin and end in double quotes. If it does not, the caller may throw an
	 *         exception.
	 * @throws InvocationNotExpressableException if the argument cannot be quoted.
	 */
	public String quote(String argument);
}
