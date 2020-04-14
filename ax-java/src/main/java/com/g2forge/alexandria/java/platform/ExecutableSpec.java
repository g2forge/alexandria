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
	None(null, null);

	protected final String prefix;

	protected final String suffix;
}
