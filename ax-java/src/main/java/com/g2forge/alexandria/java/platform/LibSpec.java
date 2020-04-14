package com.g2forge.alexandria.java.platform;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor
public enum LibSpec implements IPlatformNamed {
	Unknown(null, null),
	DLL(null, ".dll"),
	SO("lib", ".so");

	protected final String prefix;

	protected final String suffix;

	@Override
	public String getPlatformName(String name) {
		return IPlatformNamed.getPlatformName(getPrefix(), name, getSuffix());
	}
}
