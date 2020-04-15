package com.g2forge.alexandria.java.platform;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LibrarySpec implements IPlatformNamed {
	Unknown(null, null),
	DLL(null, ".dll"),
	SO("lib", ".so");

	protected final String prefix;

	protected final String suffix;
}
