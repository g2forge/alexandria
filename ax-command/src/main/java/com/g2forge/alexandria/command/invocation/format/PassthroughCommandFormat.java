package com.g2forge.alexandria.command.invocation.format;

import com.g2forge.alexandria.java.core.marker.ISingleton;

public class PassthroughCommandFormat implements ICommandFormat, ISingleton {
	protected static final PassthroughCommandFormat INSTANCE = new PassthroughCommandFormat();

	public static PassthroughCommandFormat create() {
		return INSTANCE;
	}

	private PassthroughCommandFormat() {}

	@Override
	public String quote(String argument) {
		return argument;
	}
}
