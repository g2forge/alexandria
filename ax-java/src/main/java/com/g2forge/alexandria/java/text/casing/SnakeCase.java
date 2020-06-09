package com.g2forge.alexandria.java.text.casing;

import java.util.regex.Pattern;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SnakeCase extends ACase {
	public static final ICase DASH = new SnakeCase("-");

	public static final ICase UNDERSCORE = new SnakeCase("_");
	
	public static final ICase SPACE = new SnakeCase(" ");

	protected final String tokenSeparator;

	@Override
	protected String convertToken(CasedToken token) {
		return token.getString().toLowerCase();
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
