package com.g2forge.alexandria.java.text.quote;

import com.g2forge.alexandria.java.text.escape.BashEscapeType;
import com.g2forge.alexandria.java.text.escape.IEscapeType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BashQuoteType implements IQuoteType {
	BashSingle(HQuote.QUOTE_SINGLE, BashEscapeType.Single),
	BashDoubleExpand(HQuote.QUOTE_DOUBLE, BashEscapeType.DoubleExpand),
	BashDoubleNoExpand(HQuote.QUOTE_DOUBLE, BashEscapeType.DoubleNoExpand);

	protected static final String OPCHARACTERS = "|&;()<>!#";

	protected static final String WHITESPACE = " \t\n\r";

	protected final String prefix;

	protected final IEscapeType escapeType;

	@Override
	public String getPostfix() {
		return getPrefix();
	}

	@Override
	public boolean isQuoteNeeded(CharSequence string) {
		if (HQuote.QUOTE_SINGLE.contentEquals(string) || HQuote.QUOTE_DOUBLE.contentEquals(string)) return true;
		boolean containsOpCharacters = false, containsNonOpCharacters = false;
		for (int i = 0; i < string.length(); i++) {
			final char character = string.charAt(i);
			// If we find whitespace this thing definitely needs to be quoted
			for (int j = 0; j < WHITESPACE.length(); j++) {
				if (WHITESPACE.charAt(j) == character) return true;
			}

			boolean isOpCharacter = false;
			for (int j = 0; j < OPCHARACTERS.length(); j++) {
				if (OPCHARACTERS.charAt(j) == character) {
					isOpCharacter = true;
					break;
				}
			}
			if (isOpCharacter) containsOpCharacters = true;
			else containsNonOpCharacters = true;

			if (containsOpCharacters && containsNonOpCharacters) return true;
		}
		return false;
	}
}