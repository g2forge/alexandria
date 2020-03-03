package com.g2forge.alexandria.java.text.escape;

import com.g2forge.alexandria.java.core.marker.ISingleton;

public class BashSingleEscapeType implements IEscapeType, ISingleton {
	protected static final BashSingleEscapeType INSTANCE = new BashSingleEscapeType();

	public static BashSingleEscapeType create() {
		return INSTANCE;
	}

	@Override
	public String escape(final String escapesRegex, final String string) {
		return string.replaceAll(escapesRegex, "'\"$0\"'");
	}

	@Override
	public String unescape(final String escapesRegex, final String string) {
		return string.replaceAll("'\"(" + escapesRegex + ")\"'", "$1");
	}
}
