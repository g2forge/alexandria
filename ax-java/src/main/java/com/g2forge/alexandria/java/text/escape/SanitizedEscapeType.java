package com.g2forge.alexandria.java.text.escape;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.g2forge.alexandria.java.function.IFunction1;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SanitizedEscapeType implements IEscapeType {
	protected final String prefix = "%";

	protected final String characters;

	protected final IFunction1<Character, String> replacement;

	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private final Map<String, Character> unescapeMap = computeUnescapeMap();

	public SanitizedEscapeType(String characters) {
		this(characters, c -> Integer.toHexString((int) c));
	}

	protected Map<String, Character> computeUnescapeMap() {
		final Map<String, Character> retVal = new LinkedHashMap<>();
		for (char character : getCharacters().toCharArray()) {
			final String replacement = getReplacement().apply(character);
			retVal.put(replacement, character);
		}
		return retVal;
	}

	@Override
	public IEscaper getEscaper() {
		return new AEscaper(getPrefix(), null) {
			@Override
			protected Change computeEscape(String string, int index) {
				final int matchIndex = getCharacters().indexOf(string.charAt(index));
				if (matchIndex < 0) return null;
				return new Change(1, c -> getReplacement().apply(c.charAt(0)));
			}

			@Override
			protected Pattern computeRequiresEscapePattern() {
				throw new UnsupportedOperationException();
			}

			@Override
			protected Change computeUnescape(String string, int index) {
				for (Map.Entry<String, Character> entry : getUnescapeMap().entrySet()) {
					if (string.startsWith(entry.getKey(), index)) return new Change(entry.getKey().length(), IFunction1.create(entry.getValue()));
				}
				return null;
			}
		};
	}
}
