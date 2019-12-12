package com.g2forge.alexandria.java.text.quote;

import com.g2forge.alexandria.java.core.marker.ISingleton;

public class BashSingleEscapeType implements IEscapeType, ISingleton {
	protected static final BashSingleEscapeType INSTANCE = new BashSingleEscapeType();

	public static BashSingleEscapeType create() {
		return INSTANCE;
	}

	@Override
	public String escape(final String escapes, final String argument) {
		return argument.replaceAll(escapes, "'\"$0\"'");
	}

	@Override
	public String unescape(final String escapes, final String argument) {
		return argument.replaceAll("'\"(" + escapes + ")\"'", "$1");
	}
}
