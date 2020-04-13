package com.g2forge.alexandria.java.text.quote;

import com.g2forge.alexandria.java.text.escape.BashEscapeType;
import com.g2forge.alexandria.java.text.escape.IEscapeType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BashQuoteType implements IEnumQuoteType<BashQuoteType> {
	BashSingle(HQuote.QUOTE_SINGLE, BashEscapeType.Single),
	BashDoubleExpand(HQuote.QUOTE_DOUBLE, BashEscapeType.DoubleExpand),
	BashDoubleNoExpand(HQuote.QUOTE_DOUBLE, BashEscapeType.DoubleNoExpand);

	protected static final String OPCHARACTERS = "|&;()<>!#";

	protected static final String WHITESPACE = " \t\n\r";

	protected static final String FORCE_QUOTE = OPCHARACTERS + WHITESPACE + HQuote.QUOTE_DOUBLE + HQuote.QUOTE_SINGLE;

	protected final String prefix;

	protected final IEscapeType escapeType;

	@Override
	public String getPostfix() {
		return getPrefix();
	}

	@Override
	public boolean isQuoteNeeded(CharSequence string) {
		for (int i = 0; i < string.length(); i++) {
			final char character = string.charAt(i);
			if (FORCE_QUOTE.indexOf(character) >= 0) return true;
		}
		return false;
	}
}