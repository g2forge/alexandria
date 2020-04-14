package com.g2forge.alexandria.java.platform;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UARTSpec implements IPlatformNamed {
	Unknown(null, null),
	COM("COM", null),
	ttyS("ttyS", null),
	ttyUSB("ttyUSB", null);

	protected final String prefix;

	protected final String suffix;
}
