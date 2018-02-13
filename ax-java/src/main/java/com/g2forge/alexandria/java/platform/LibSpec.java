package com.g2forge.alexandria.java.platform;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LibSpec implements IPlatformNamed {
	Unknown(null, null),
	DLL(null, ".dll"),
	SO("lib", ".so");

	protected final String prefix;

	protected final String suffix;

	@Override
	public String getPlatformName(String base) {
		return APlatformNamed.getPlatformName(prefix, base, suffix);
	}
}
