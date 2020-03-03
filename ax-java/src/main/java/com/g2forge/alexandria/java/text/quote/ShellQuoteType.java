package com.g2forge.alexandria.java.text.quote;

import com.g2forge.alexandria.java.text.escape.BackslashEscapeType;
import com.g2forge.alexandria.java.text.escape.BashSingleEscapeType;
import com.g2forge.alexandria.java.text.escape.IEscapeType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShellQuoteType implements IRegexQuoteType {
	BashSingle(HQuote.QUOTE_SINGLE, "'", "[`'\\\\ &!#]", BashSingleEscapeType.create()),
	BashDoubleNoExpand(HQuote.QUOTE_DOUBLE, "[$`\"\\\\]", "[ &!#]", BackslashEscapeType.create()),
	BashDoubleExpand(HQuote.QUOTE_DOUBLE, "[\"]", "[ &!#]", BackslashEscapeType.create()),
	SedRegex("", "[\\[\\]/\\\\$*]", ".", BackslashEscapeType.create());

	protected final String prefix;

	protected final String escapesRegex;

	protected final String quoteRegex;

	protected final IEscapeType escapeType;

	@Override
	public String getPostfix() {
		return getPrefix();
	}
}