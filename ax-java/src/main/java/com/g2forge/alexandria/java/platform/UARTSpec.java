package com.g2forge.alexandria.java.platform;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor
public enum UARTSpec implements IPlatformNamed {
	Unknown(null, null),
	COM("COM", null),
	ttyS("ttyS", null),
	ttyUSB("ttyUSB", null);

	protected final String prefix;

	protected final String suffix;

	@Override
	public String getPlatformName(String base) {
		return IPlatformNamed.getPlatformName(getPrefix(), base, getSuffix());
	}
}
