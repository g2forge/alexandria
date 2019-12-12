package com.g2forge.alexandria.java.text.quote;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AQuoteType implements IQuoteType {
	protected final String start;

	protected final String escapes;

	protected final String quote;

	protected final String end;

	protected final IEscapeType escapeType;
}