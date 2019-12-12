package com.g2forge.alexandria.java.text.quote;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShellQuoteType implements IQuoteType {
	BashSingle(HQuote.QUOTE_SINGLE, "'", "[`'\\\\ &!#]", BashSingleEscapeType.INSTANCE),
	BashDoubleNoExpand(HQuote.QUOTE_DOUBLE, "[$`\"\\\\]", "[ &!#]", BackslashEscapeType.INSTANCE),
	BashDoubleExpand(HQuote.QUOTE_DOUBLE, "[\"]", "[ &!#]", BackslashEscapeType.INSTANCE),
	SedRegex("", "[\\[\\]/\\\\$*]", ".", BackslashEscapeType.INSTANCE);

	protected final String start;

	protected final String escapes;

	protected final String quote;

	protected final IEscapeType escapeType;

	@Override
	public String getEnd() {
		return getStart();
	}
}