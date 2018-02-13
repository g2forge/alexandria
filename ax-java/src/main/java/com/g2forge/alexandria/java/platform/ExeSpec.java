package com.g2forge.alexandria.java.platform;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ExeSpec implements IPlatformNamed {
	Unknown(null, null),
	EXE(null, ".exe"),
	BAT(null, ".bat"),
	SH(null, ".sh"),
	None(null, null);

	protected final String prefix;

	protected final String suffix;

	@Override
	public String getPlatformName(String _base) {
		return APlatformNamed.getPlatformName(prefix, _base, suffix);
	}
}
