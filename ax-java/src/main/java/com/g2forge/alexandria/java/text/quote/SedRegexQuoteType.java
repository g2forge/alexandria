package com.g2forge.alexandria.java.text.quote;

import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.java.text.escape.EscapeType;
import com.g2forge.alexandria.java.text.escape.IEscapeType;
import com.g2forge.alexandria.java.text.escape.SingleCharacterEscaper;

public class SedRegexQuoteType implements IRegexQuoteType, ISingleton {
	protected static final SedRegexQuoteType INSTANCE = new SedRegexQuoteType();

	private SedRegexQuoteType() {}

	@Override
	public IEscapeType getEscapeType() {
		return new EscapeType(new SingleCharacterEscaper("\\", null, "[]/\\$*", null, -1));
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