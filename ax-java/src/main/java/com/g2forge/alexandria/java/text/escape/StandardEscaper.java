package com.g2forge.alexandria.java.text.escape;

import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.text.TextUpdate;
import com.g2forge.alexandria.java.text.TextUpdateBuilder;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class StandardEscaper implements IEscaper {
	protected final String prefix;

	protected final String postfix;

	protected final String unescaped;

	protected final String escaped;

	protected final int required;

	/**
	 * Create a standard escaper.
	 * 
	 * @param prefix A string which prefixes all escape sequences.
	 * @param postfix A string which postfixes all escape sequences.
	 * @param unescaped A string containing all the characters to escape. This is not a regex, and does not provide support for escaping multi-character
	 *            sequences.
	 * @param escaped A string containing the escaped representations of the unescaped characters (without any prefix or postfix). May be {@code null} if
	 *            there's no translation required.
	 * @param required Indicates that the first {@code required} characters of {@code unescaped} indicate characters which must be escaped, while those after
	 *            that may be. Can be {@code -1} to indicate that all of them must be escaped.
	 */
	public StandardEscaper(String prefix, String postfix, String unescaped, String escaped, int required) {
		if ((unescaped == null) || (unescaped.length() < 1)) throw new IllegalArgumentException();
		if ((escaped != null) && (unescaped.length() != escaped.length())) throw new IllegalArgumentException(String.format("List of unescaped (%1$s) and escaped (%2$s) characters must be the same length!", unescaped, escaped));

		this.prefix = prefix;
		this.postfix = postfix;
		this.unescaped = unescaped;
		this.escaped = escaped;
		this.required = required;
	}

	@Override
	public String escape(String string) {
		final IFunction1<CharSequence, String> prefix = getPrefix() == null ? null : IFunction1.create(getPrefix());
		final IFunction1<CharSequence, String> postfix = getPostfix() == null ? null : IFunction1.create(getPostfix());

		final TextUpdateBuilder builder = new TextUpdateBuilder(string);
		int startCopy = -1;
		for (int i = 0; i < string.length(); i++) {
			final char c = string.charAt(i);
			final int index = unescaped.indexOf(c);
			if ((index >= 0) && ((required < 0) || (index < required))) {
				if (startCopy >= 0) builder.accept(new TextUpdate<>(startCopy, i - startCopy, IFunction1.identity()));
				startCopy = -1;

				if (prefix != null) builder.accept(new TextUpdate<>(i, 0, prefix));
				builder.accept(new TextUpdate<Object>(i, 1, escaped == null ? IFunction1.identity() : x -> escaped.charAt(index)));
				if (postfix != null) builder.accept(new TextUpdate<>(i, 0, postfix));
			} else if (startCopy < 0) startCopy = i;
		}
		if (startCopy >= 0) builder.accept(new TextUpdate<>(startCopy, string.length() - startCopy, IFunction1.identity()));
		return builder.build();
	}

	@Override
	public String getCharactersRequiringEscape() {
		final String unescaped = getUnescaped();
		final int required = getRequired();
		if (required < 0) return unescaped;
		return unescaped.substring(0, required);
	}

	@Override
	public String unescape(String string) {
		final String prefix = getPrefix(), postfix = getPostfix();
		final int prefixLength = prefix == null ? 0 : prefix.length(), postfixLength = postfix == null ? 0 : postfix.length();

		final TextUpdateBuilder builder = new TextUpdateBuilder(string);
		int startCopy = -1;
		for (int i = 0; i < string.length(); i++) {
			if ((prefix == null) || string.startsWith(prefix, i)) {
				final char c = string.charAt(i + prefixLength);
				final int index = (escaped == null ? unescaped : escaped).indexOf(c);
				if (index >= 0) {
					if ((postfix == null) || string.startsWith(postfix, i + prefixLength + 1)) {
						if (startCopy >= 0) builder.accept(new TextUpdate<>(startCopy, i - startCopy, IFunction1.identity()));
						startCopy = -1;

						builder.accept(new TextUpdate<Object>(i + prefixLength, 1, escaped == null ? IFunction1.identity() : x -> unescaped.charAt(index)));
						i += prefixLength + postfixLength;
						continue;
					}
				}
			}
			if (startCopy < 0) startCopy = i;
		}
		if (startCopy >= 0) builder.accept(new TextUpdate<>(startCopy, string.length() - startCopy, IFunction1.identity()));
		return builder.build();
	}
}