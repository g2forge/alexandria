package com.g2forge.alexandria.java.text.quote;

import com.g2forge.alexandria.java.text.escape.IEscapeType;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class RegexQuoteType implements IRegexQuoteType {
	protected final String prefix;

	protected final String escapesRegex;

	protected final String quoteRegex;

	protected final String postfix;

	protected final IEscapeType escapeType;
}