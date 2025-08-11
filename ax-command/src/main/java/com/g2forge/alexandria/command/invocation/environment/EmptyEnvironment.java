package com.g2forge.alexandria.command.invocation.environment;

import java.util.Map;

import com.g2forge.alexandria.java.core.helpers.HMap;
import com.g2forge.alexandria.java.core.marker.ISingleton;

public class EmptyEnvironment implements IEnvironment, ISingleton {
	protected static final EmptyEnvironment INSTANCE = new EmptyEnvironment();

	public static EmptyEnvironment create() {
		return INSTANCE;
	}

	protected EmptyEnvironment() {}

	@Override
	public String apply(String name) {
		return null;
	}

	@Override
	public Map<String, String> toMap() {
		return HMap.empty();
	}
}
