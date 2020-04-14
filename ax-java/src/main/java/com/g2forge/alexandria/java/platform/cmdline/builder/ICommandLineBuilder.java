package com.g2forge.alexandria.java.platform.cmdline.builder;

import java.util.List;

import com.g2forge.alexandria.java.platform.cmdline.format.ICommandFormat;

public interface ICommandLineBuilder {
	/**
	 * Pre-process a command line before specifying it to {@link ProcessBuilder#command(List)}. This is required in order to ensure OS specific quoting rules
	 * are properly applied. In short: the Java runtime has some truly horrifying bugs stemming from the fact that the API does not support the
	 * <a href="https://docs.microsoft.com/en-us/windows/win32/api/processthreadsapi/nf-processthreadsapi-createprocessa">Win32 CreateProcess API</a> properly.
	 * In particular {@link ProcessBuilder} assumes that commands are a list of strings, rather than (as under Win32) a
	 * <a href="https://docs.microsoft.com/en-us/windows/win32/api/processenv/nf-processenv-getcommandlinea">single command line string</a>.
	 * 
	 * @param format The command specific format for the command line.
	 * @param line The command and arguments.
	 * @return The command line, suitably modified.
	 */
	public List<String> build(ICommandFormat format, List<String> line);
}
