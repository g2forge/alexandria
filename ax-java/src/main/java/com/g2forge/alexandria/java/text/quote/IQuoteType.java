package com.g2forge.alexandria.java.text.quote;

import java.util.regex.Pattern;

public interface IQuoteType {
	public default String escape(final String text) {
		return getEscapeType().escape(getEscapes(), text);
	}

	public String getEnd();

	/**
	 * Get the regex pattern for character sequences which need to be escaped. Escaping will be done with {@link #escape(String)}.
	 * 
	 * @return A regex pattern for sequences to escape.
	 */
	public String getEscapes();

	public IEscapeType getEscapeType();

	/**
	 * Get the regex pattern for character sequences whose presence indicate that a string must be quoted.
	 * 
	 * @return A regex pattern to determine whether a string must be quoted.
	 */
	public String getQuote();

	public String getStart();

	public default boolean isQuote(final String argument) {
		return Pattern.compile(getEscapes()).matcher(argument).find() || Pattern.compile(getQuote()).matcher(argument).find();
	}

	public default boolean isQuoted(final String argument) {
		return argument.startsWith(getStart()) && argument.endsWith(getEnd());
	}

	public default String quote(final QuoteControl option, final String text, IQuoteType... otherQuoteTypes) {
		if (QuoteControl.Never.equals(option)) return text;
		if (QuoteControl.IfNotAlready.equals(option)) {
			for (final IQuoteType anytype : otherQuoteTypes) {
				if (anytype.isQuoted(text)) return text;
			}
		}

		if ((option != QuoteControl.IfNeeded) || isQuote(text)) {
			final StringBuilder builder = new StringBuilder();
			builder.append(getStart());
			builder.append(escape(text));
			builder.append(getEnd());
			return builder.toString();
		}

		return text;
	}

	public default String unescape(final String text) {
		return getEscapeType().unescape(getEscapes(), text);
	}

	public default String unquote(final String text) {
		if (isQuoted(text)) { return unescape(text.substring(getStart().length(), text.length() - getEnd().length())); }
		return text;
	}
}