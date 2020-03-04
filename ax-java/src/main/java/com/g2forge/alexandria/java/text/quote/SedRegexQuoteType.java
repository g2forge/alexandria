package com.g2forge.alexandria.java.text.quote;

import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.java.text.escape.BackslashEscapeType;
import com.g2forge.alexandria.java.text.escape.IEscapeType;

public class SedRegexQuoteType implements IRegexQuoteType, ISingleton {
	protected static final SedRegexQuoteType INSTANCE = new SedRegexQuoteType();

	private SedRegexQuoteType() {}

	@Override
	public String getEscapesRegex() {
		return "[\\[\\]/\\\\$*]";
	}

	@Override
	public IEscapeType getEscapeType() {
		return BackslashEscapeType.create();
	}

	@Override
	public String getPostfix() {
		return getPrefix();
	}

	@Override
	public String getPrefix() {
		return "";
	}

	@Override
	public String getQuoteRegex() {
		return ".";
	}
}