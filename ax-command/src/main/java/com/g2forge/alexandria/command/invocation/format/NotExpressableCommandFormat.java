package com.g2forge.alexandria.command.invocation.format;

import com.g2forge.alexandria.java.core.marker.ISingleton;

public class NotExpressableCommandFormat implements ICommandFormat, ISingleton {
	protected static final NotExpressableCommandFormat INSTANCE = new NotExpressableCommandFormat();

	public static NotExpressableCommandFormat create() {
		return INSTANCE;
	}

	private NotExpressableCommandFormat() {}

	@Override
	public String quote(String argument) {
		throw new InvocationNotExpressableException("There is no way to quote the arguments for this command, due to bugs in the Java runtime library for Win32! See the javadocs for Win32CommandLineBuilder.build(...) for more information.");
	}
}
