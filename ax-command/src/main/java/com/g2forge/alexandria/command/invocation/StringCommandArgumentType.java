package com.g2forge.alexandria.command.invocation;

import com.g2forge.alexandria.java.core.marker.ISingleton;

public class StringCommandArgumentType implements ICommandArgumentType<String>, ISingleton {
	protected static final StringCommandArgumentType INSTANCE = new StringCommandArgumentType();

	public static StringCommandArgumentType create() {
		return INSTANCE;
	}

	protected StringCommandArgumentType() {}

	@Override
	public String create(String string) {
		return string;
	}

	@Override
	public String create(String string, String original) {
		return string;
	}

	@Override
	public String get(String argument) {
		return argument;
	}
}
