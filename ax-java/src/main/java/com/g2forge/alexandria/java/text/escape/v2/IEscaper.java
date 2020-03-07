package com.g2forge.alexandria.java.text.escape.v2;

public interface IEscaper {
	/**
	 * Escape some text.
	 * 
	 * @param string The raw text to be escaped.
	 * @return The escaped text.
	 */
	public String escape(final String string);

	/**
	 * Unescape some text.
	 * 
	 * @param string The text to be unescaped.
	 * @return The raw text.
	 */
	public String unescape(final String string);
}
