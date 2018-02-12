package com.g2forge.alexandria.java.platform;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UARTSpec implements IPlatformNamed {
	Unknown(null, null),
	COM("COM", null),
	ttyS("ttyS", null),
	ttyUSB("ttyUSB", null);

	protected final String prefix;

	protected final String suffix;

	@Override
	public String getPlatformName(String base) {
		return APlatformNamed.getPlatformName(prefix, base, suffix);
	}
}
