package com.g2forge.alexandria.java.text.quote;

import java.util.regex.Pattern;

public interface IRegexQuoteType extends IQuoteType {
	@Override
	public default String escape(final String string) {
		return getEscapeType().escape(getEscapesRegex(), string);
	}

	/**
	 * Get the regex pattern for character sequences which need to be escaped. Escaping will be done with {@link #escape(String)}.
	 * 
	 * @return A regex pattern for sequences to escape.
	 */
	public String getEscapesRegex();

	/**
	 * Get the regex pattern for character sequences whose presence indicate that a string must be quoted.
	 * 
	 * @return A regex pattern to determine whether a string must be quoted.
	 */
	public String getQuoteRegex();

	@Override
	public default boolean isQuoteNeeded(final String string) {
		return Pattern.compile(getEscapesRegex()).matcher(string).find() || Pattern.compile(getQuoteRegex()).matcher(string).find();
	}

	@Override
	public default String unescape(final String string) {
		return getEscapeType().unescape(getEscapesRegex(), string);
	}
}
