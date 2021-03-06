package com.g2forge.alexandria.java.text.quote;

import java.util.regex.Pattern;

public interface IRegexQuoteType extends IQuoteType {
	/**
	 * Get the regex pattern for character sequences whose presence indicate that a string must be quoted.
	 * 
	 * @return A regex pattern to determine whether a string must be quoted.
	 */
	public String getQuoteRegex();

	@Override
	public default boolean isQuoteNeeded(final CharSequence string) {
		final Pattern pattern = getEscapeType().getEscaper().getRequiresEscapePattern();
		return pattern.matcher(string).find() || Pattern.compile(getQuoteRegex()).matcher(string).find();
	}
}
