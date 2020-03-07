package com.g2forge.alexandria.java.text.quote;

import com.g2forge.alexandria.java.text.escape.BackslashEscapeType;
import com.g2forge.alexandria.java.text.escape.IEscapeType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JavaQuoteType implements IQuoteType {
	String(HQuote.QUOTE_DOUBLE, "\t\b\n\r\f\'\"\\", BackslashEscapeType.create());

	protected final String prefix;

	protected final String escapesRegex;

	protected final IEscapeType escapeType;

	@Override
	public String getPostfix() {
		return getPrefix();
	}

	@Override
	public boolean isQuoteNeeded(CharSequence string) {
		return true;
	}
}