package com.g2forge.alexandria.java.platform;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor
public enum ExeSpec implements IPlatformNamed {
	Unknown(null, null),
	EXE(null, ".exe"),
	BAT(null, ".bat"),
	SH(null, ".sh"),
	None(null, null);

	protected final String prefix;

	protected final String suffix;

	@Override
	public String getPlatformName(String name) {
		return IPlatformNamed.getPlatformName(prefix, name, suffix);
	}
}
