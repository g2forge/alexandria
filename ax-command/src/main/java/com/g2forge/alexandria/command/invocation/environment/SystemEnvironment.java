package com.g2forge.alexandria.command.invocation.environment;

import java.util.Map;

import com.g2forge.alexandria.java.core.marker.ISingleton;

public class SystemEnvironment implements IEnvironment, ISingleton {
	protected static final SystemEnvironment INSTANCE = new SystemEnvironment();

	public static SystemEnvironment create() {
		return INSTANCE;
	}

	protected SystemEnvironment() {}

	@Override
	public String apply(String name) {
		return System.getenv(name);
	}

	@Override
	public Map<String, String> toMap() {
		return System.getenv();
	}
}
