package com.g2forge.alexandria.java.text.quote;

import com.g2forge.alexandria.java.text.escape.IEscapeType;
import com.g2forge.alexandria.java.text.escape.JavaEscapeType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JavaQuoteType implements IQuoteType {
	String(HQuote.QUOTE_DOUBLE, HQuote.QUOTE_DOUBLE, JavaEscapeType.String),
	PatternCharClass("[", "]", JavaEscapeType.PatternCharClass);

	protected final String prefix;
	
	protected final String postfix;

	protected final IEscapeType escapeType;

	@Override
	public boolean isQuoteNeeded(CharSequence string) {
		return true;
	}
}