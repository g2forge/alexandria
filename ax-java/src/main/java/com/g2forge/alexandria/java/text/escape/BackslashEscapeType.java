package com.g2forge.alexandria.java.text.escape;

import com.g2forge.alexandria.java.core.marker.ISingleton;

public class BackslashEscapeType implements IEscapeType, ISingleton {
	protected static final BackslashEscapeType INSTANCE = new BackslashEscapeType();

	public static BackslashEscapeType create() {
		return INSTANCE;
	}

	public String escape(final String escapesRegex, final String string) {
		return string.replaceAll(escapesRegex, "\\\\$0");
	}

	public String unescape(final String escapesRegex, final String string) {
		return string.replaceAll("\\\\(" + escapesRegex + ")", "$1");
	}
}
