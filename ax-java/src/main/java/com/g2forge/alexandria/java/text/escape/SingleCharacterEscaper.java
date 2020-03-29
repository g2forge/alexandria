package com.g2forge.alexandria.java.text.escape;

import java.util.regex.Pattern;

import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.text.quote.JavaQuoteType;
import com.g2forge.alexandria.java.text.quote.QuoteControl;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class SingleCharacterEscaper extends AEscaper {
	protected final String unescaped;

	protected final String escaped;

	protected final int required;

	/**
	 * Create an escaper that escapes single characters by mapping them to a single character escape code, perhaps with a prefix and postfix.
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
	public SingleCharacterEscaper(String prefix, String postfix, String unescaped, String escaped, int required) {
		super(prefix, postfix);
		if ((unescaped == null) || (unescaped.length() < 1)) throw new IllegalArgumentException();
		if ((escaped != null) && (unescaped.length() != escaped.length())) throw new IllegalArgumentException(String.format("List of unescaped (%1$s) and escaped (%2$s) characters must be the same length!", unescaped, escaped));

		this.unescaped = unescaped;
		this.escaped = escaped;
		this.required = required;
	}

	@Override
	protected Change computeEscape(String string, int index) {
		final int matchIndex = unescaped.indexOf(string.charAt(index));
		if (matchIndex < 0) return null;
		if ((required >= 0) && (matchIndex >= required)) return null;
		return new Change(1, c -> (escaped == null) ? c : escaped.charAt(matchIndex));
	}

	@Override
	protected Pattern computeRequiresEscapePattern() {
		final String unescaped = getUnescaped();
		final int required = getRequired();
		final String charactersRequiringEscape = (required < 0) ? unescaped : unescaped.substring(0, required);
		return Pattern.compile(JavaQuoteType.PatternCharClass.quote(QuoteControl.Always, charactersRequiringEscape));
	}

	@Override
	protected Change computeUnescape(String string, int index) {
		final int matchIndex = (escaped == null ? unescaped : escaped).indexOf(string.charAt(index));
		if (matchIndex < 0) return null;
		return new Change(1, escaped == null ? IFunction1.identity() : IFunction1.create(unescaped.charAt(matchIndex)));
	}
}