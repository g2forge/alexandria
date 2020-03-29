package com.g2forge.alexandria.java.text.escape;

import java.util.regex.Pattern;

import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.text.TextUpdate;
import com.g2forge.alexandria.java.text.TextUpdateBuilder;

public interface IEscaper {
	public void computeEscape(String string, final IConsumer1<? super TextUpdate<?>> consumer);

	public void computeUnescape(String string, final IConsumer1<? super TextUpdate<?>> consumer);

	/**
	 * Escape some text.
	 * 
	 * @param string The raw text to be escaped.
	 * @return The escaped text.
	 */
	public default String escape(String string) {
		final TextUpdateBuilder builder = new TextUpdateBuilder(string);
		computeEscape(string, builder);
		return builder.build();
	}

	public Pattern getRequiresEscapePattern();

	/**
	 * Unescape some text.
	 * 
	 * @param string The text to be unescaped.
	 * @return The raw text.
	 */
	public default String unescape(String string) {
		final TextUpdateBuilder builder = new TextUpdateBuilder(string);
		computeUnescape(string, builder);
		return builder.build();
	}
}
