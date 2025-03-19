package com.g2forge.alexandria.match;

import com.g2forge.alexandria.java.function.IPredicate1;
import com.g2forge.alexandria.java.text.HString;
import com.hrakaroo.glob.GlobPattern;
import com.hrakaroo.glob.MatchingEngine;

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
public class GlobMatch implements IPredicate1<String> {
	public static final String PREFIX_GLOB = "glob:";

	protected final String value;

	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private final MatchingEngine matchingEngine = computeMatchingEngine();

	protected MatchingEngine computeMatchingEngine() {
		final String glob = HString.stripPrefix(getValue(), GlobMatch.PREFIX_GLOB);
		return GlobPattern.compile(glob);
	}

	@Override
	public boolean test(String string) {
		return string != null && getMatchingEngine().matches(string);
	}
}
