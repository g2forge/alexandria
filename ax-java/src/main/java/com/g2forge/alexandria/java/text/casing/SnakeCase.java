package com.g2forge.alexandria.java.text.casing;

import java.util.regex.Pattern;

import com.g2forge.alexandria.java.text.HString;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SnakeCase extends ACase {
	public static final ICase DASH = new SnakeCase("-", false);

	public static final ICase UNDERSCORE = new SnakeCase("_", false);

	public static final ICase SPACE = new SnakeCase(" ", false);

	public static final ICase SPACE_CASED = new SnakeCase(" ", true);

	protected final String tokenSeparator;

	protected final boolean cased;

	@Override
	protected String convertToken(CasedToken token) {
		final String string = token.getString();
		if (cased) {
			if (HString.hasUppercase(string)) return string;
			return Character.toUpperCase(string.charAt(0)) + string.substring(1);
		} else return string.toLowerCase();
	}

	@Override
	protected Pattern getTokenPattern() {
		return Pattern.compile("(.*?)(" + Pattern.quote(getTokenSeparator()) + "|$)");
	}

	@Override
	protected CasedToken.Type getType(String string) {
		return CasedToken.Type.Unknown;
	}
}
