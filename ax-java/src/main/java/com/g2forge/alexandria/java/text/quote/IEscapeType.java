package com.g2forge.alexandria.java.text.quote;

/**
 * Represents a certain style of character sequence escaping.
 */
public interface IEscapeType {
	/**
	 * Escape some text.
	 * 
	 * @param escapes A regex which matches the character sequences to escape.
	 * @param text The text to be escaped.
	 * @return The escaped text.
	 */
	public String escape(final String escapes, final String text);

	/**
	 * Unescape some text.
	 * 
	 * @param escapes A regex which matches the character sequences which are escaped.
	 * @param text The text to be unescaped.
	 * @return The raw text.
	 */
	public String unescape(final String escapes, final String text);
}
