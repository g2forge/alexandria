package com.g2forge.alexandria.java.text.escape;

public interface IEscaper {
	/**
	 * Escape some text.
	 * 
	 * @param string The raw text to be escaped.
	 * @return The escaped text.
	 */
	public String escape(final String string);

	public String getCharactersRequiringEscape();

	/**
	 * Unescape some text.
	 * 
	 * @param string The text to be unescaped.
	 * @return The raw text.
	 */
	public String unescape(final String string);
}
