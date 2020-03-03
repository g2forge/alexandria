package com.g2forge.alexandria.java.text.escape;

/**
 * Represents a certain style of character sequence escaping.
 */
public interface IEscapeType {
	/**
	 * Escape some text.
	 * 
	 * @param escapesRegex A regex which matches the character sequences to escape.
	 * @param string The text to be escaped.
	 * @return The escaped text.
	 */
	public String escape(final String escapesRegex, final String string);

	/**
	 * Unescape some text.
	 * 
	 * @param escapesRegex A regex which matches the character sequences which are escaped.
	 * @param string The text to be unescaped.
	 * @return The raw text.
	 */
	public String unescape(final String escapesRegex, final String string);
}
