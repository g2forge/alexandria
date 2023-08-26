package com.g2forge.alexandria.java.text.casing;

import java.util.List;
import java.util.regex.Pattern;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.text.HString;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SnakeCase extends ACase {
	public static final SnakeCase DASH = new SnakeCase("-");

	public static final SnakeCase UNDERSCORE = new SnakeCase("_");

	public static final SnakeCase SPACE = new SnakeCase(" ");

	public static final SnakeCase SPACE_CASED = new SnakeCase(" ", true);

	protected final String tokenSeparator;

	protected final boolean cased;

	protected final List<String> otherSeparators;

	@Getter(lazy = true)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private final Pattern tokenPattern = computeTokenPattern();

	public SnakeCase(String tokenSeparator) {
		this(tokenSeparator, false);
	}

	public SnakeCase(String tokenSeparator, boolean cased, String... otherSeparators) {
		this(tokenSeparator, cased, HCollection.asList(otherSeparators));
	}

	protected Pattern computeTokenPattern() {
		final StringBuilder pattern = new StringBuilder();
		pattern.append("(.*?)(");
		final boolean hasOthers = (getOtherSeparators() != null) && !getOtherSeparators().isEmpty();
		if (hasOthers) pattern.append('(');
		pattern.append(Pattern.quote(getTokenSeparator()));
		if (hasOthers) {
			for (String otherSeparator : getOtherSeparators()) {
				pattern.append('|').append(Pattern.quote(otherSeparator));
			}
			pattern.append(')');
		}
		pattern.append("|$)");
		return Pattern.compile(pattern.toString());
	}

	@Override
	protected String convertToken(CasedToken token) {
		final String string = token.getString();
		if (cased) {
			if (HString.hasUppercase(string)) return string;
			return Character.toUpperCase(string.charAt(0)) + string.substring(1);
		} else return string.toLowerCase();
	}

	@Override
	protected CasedToken.Type getType(String string) {
		return CasedToken.Type.Unknown;
	}
}
