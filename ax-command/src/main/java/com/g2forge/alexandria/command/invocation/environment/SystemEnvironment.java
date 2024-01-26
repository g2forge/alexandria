package com.g2forge.alexandria.command.invocation.environment;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.java.platform.HPlatform;

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
		final Map<String, String> systemEnvironment = getSystemEnvironment();

		if (isCaseInsensitive()) {
			final TreeMap<String, String> retVal = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
			retVal.putAll(systemEnvironment);
			return Collections.unmodifiableSortedMap(retVal);
		}
		return systemEnvironment;
	}

	protected Map<String, String> getSystemEnvironment() {
		return System.getenv();
	}

	protected boolean isCaseInsensitive() {
		return HPlatform.getPlatform().getPathSpec().isCaseSensitive();
	}
}
