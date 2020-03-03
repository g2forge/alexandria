package com.g2forge.alexandria.java.text.quote;

import com.g2forge.alexandria.java.text.escape.IEscapeType;

public interface IQuoteType {
	public String escape(final String string);

	public IEscapeType getEscapeType();

	/**
	 * The postfix to use when quoting a string. For example this may be a single double quote character.
	 * 
	 * @return the postfix to use when quoting a string.
	 */
	public String getPostfix();

	/**
	 * The prefix to use when quoting a string. For example this may be a single double quote character.
	 * 
	 * @return the prefix to use when quoting a string.
	 */
	public String getPrefix();

	public default boolean isQuoted(final String string) {
		return string.startsWith(getPrefix()) && string.endsWith(getPostfix());
	}

	public boolean isQuoteNeeded(final String string);

	public default String quote(final QuoteControl option, final String string, IQuoteType... otherQuoteTypes) {
		if (QuoteControl.Never.equals(option)) return string;
		if (QuoteControl.IfNotAlready.equals(option)) {
			for (final IQuoteType anytype : otherQuoteTypes) {
				if (anytype.isQuoted(string)) return string;
			}
		}

		if ((option != QuoteControl.IfNeeded) || isQuoteNeeded(string)) {
			final StringBuilder builder = new StringBuilder();
			builder.append(getPrefix());
			builder.append(escape(string));
			builder.append(getPostfix());
			return builder.toString();
		}

		return string;
	}

	public String unescape(final String string);

	public default String unquote(final String string) {
		if (isQuoted(string)) { return unescape(string.substring(getPrefix().length(), string.length() - getPostfix().length())); }
		return string;
	}
}