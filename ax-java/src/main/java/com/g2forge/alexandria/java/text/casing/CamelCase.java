package com.g2forge.alexandria.java.text.casing;

import java.util.regex.Pattern;

import com.g2forge.alexandria.java.core.marker.ISingleton;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public class CamelCase extends ACase implements ISingleton {
	public static final CamelCase NORMAL = new CamelCase(Pattern.compile("([A-Z]([a-z0-9]+|([A-Z0-9]+((?=[A-Z][a-z])|$))))"));

	public static CamelCase create() {
		return NORMAL;
	}

	@Getter(AccessLevel.PROTECTED)
	protected final Pattern tokenPattern;

	@Override
	protected String convertToken(CasedToken token) {
		final String string = token.getString();
		switch (token.getType()) {
			case Acronym:
				return string.toUpperCase();
			case Word:
			default:
				return string.isEmpty() ? string : Character.toUpperCase(string.charAt(0)) + string.substring(1);
		}
	}

	@Override
	protected String getTokenSeparator() {
		return null;
	}

	@Override
	protected CasedToken.Type getType(String string) {
		final boolean firstCase = Character.isUpperCase(string.charAt(0));
		for (int i = 1; i < string.length(); i++) {
			if (firstCase != Character.isUpperCase(string.charAt(i))) return CasedToken.Type.Word;
		}
		return CasedToken.Type.Acronym;
	}
}
