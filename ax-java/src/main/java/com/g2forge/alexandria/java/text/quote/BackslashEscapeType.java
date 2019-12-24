package com.g2forge.alexandria.java.text.quote;

import com.g2forge.alexandria.java.core.marker.ISingleton;

public class BackslashEscapeType implements IEscapeType, ISingleton {
	protected static final BackslashEscapeType INSTANCE = new BackslashEscapeType();

	public static BackslashEscapeType create() {
		return INSTANCE;
	}

	public String escape(final String escapes, final String text) {
		return text.replaceAll(escapes, "\\\\$0");
	}

	public String unescape(final String escapes, final String text) {
		return text.replaceAll("\\\\(" + escapes + ")", "$1");
	}
}
