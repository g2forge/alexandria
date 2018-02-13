package com.g2forge.alexandria.java.text.casing;

import java.util.regex.Pattern;

import com.g2forge.alexandria.java.core.iface.ISingleton;

import lombok.ToString;

@ToString
public class CamelCase extends ACase implements ISingleton {
	protected static final CamelCase instance = new CamelCase();

	public static CamelCase create() {
		return instance;
	}

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
	protected Pattern getTokenPattern() {
		return Pattern.compile("([A-Z]([a-z0-9]+|([A-Z0-9]+((?=[A-Z][a-z])|$))))");
	}

	@Override
	protected String getTokenSeparator() {
		return null;
	}

	@Override
	protected CasedToken.Type getType(String string) {
		for (int i = 0; i < string.length(); i++) {
			if (!Character.isUpperCase(string.charAt(i))) return CasedToken.Type.Word;
		}
		return CasedToken.Type.Acronym;
	}
}
