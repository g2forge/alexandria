package com.g2forge.alexandria.match;

import java.util.regex.Pattern;

import com.g2forge.alexandria.java.function.IPredicate1;
import com.g2forge.alexandria.java.text.HString;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class RegexMatch implements IPredicate1<String> {
	public static final String PREFIX_REGEX = "regex:";

	protected final String value;

	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private final Pattern pattern = computePattern();

	protected Pattern computePattern() {
		final String regex = HString.stripPrefix(value, RegexMatch.PREFIX_REGEX);
		return Pattern.compile(regex);
	}

	@Override
	public boolean test(String string) {
		return string != null && getPattern().matcher(string).matches();
	}
}
