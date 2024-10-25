package com.g2forge.alexandria.java.platform;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExecutableSpec implements IPlatformNamed {
	Unknown(null, null),
	EXE(null, ".exe"),
	BAT(null, ".bat"),
	CMD(null, ".cmd"),
	SH(null, ".sh"),
	PS1(null, ".ps1"),
	None(null, null);

	protected final String prefix;

	protected final String suffix;

	public String fromBase(String base) {
		final StringBuilder retVal = new StringBuilder();
		if (getPrefix() != null) retVal.append(getPrefix());
		retVal.append(base);
		if (getSuffix() != null) retVal.append(getSuffix());
		return retVal.toString();
	}
}
