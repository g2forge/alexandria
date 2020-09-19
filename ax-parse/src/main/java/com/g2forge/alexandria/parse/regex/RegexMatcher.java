package com.g2forge.alexandria.parse.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.g2forge.alexandria.analysis.ISerializableFunction1;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.fluent.optional.IOptional;
import com.g2forge.alexandria.java.fluent.optional.NullableOptional;
import com.g2forge.alexandria.parse.IMatcher;
import com.g2forge.alexandria.parse.IMatcherBuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RegexMatcher<Result> implements IMatcher<Result, Regex> {
	@Getter
	@RequiredArgsConstructor
	public enum Flag {
		CASE_INSENSITIVE(Pattern.CASE_INSENSITIVE);

		protected final int flag;
	}

	public static <Parsed> IMatcherBuilder<Parsed, Regex> builder(Flag... flags) {
		return new MatcherBuilder<>(HCollection.asSet(Flag.class, flags));
	}

	protected static Object getFieldID(ISerializableFunction1<?, ?> field) {
		return field.asMethodAnalyzer().getPath();
	}

	public static <Parsed> IMatcher<Parsed, Regex> create(IMatcher<?, Regex> gap, String text, Flag... flags) {
		final IMatcherBuilder<Parsed, Regex> builder = builder(flags);
		boolean first = true;
		for (String word : text.split("\\s+")) {
			if (first) first = false;
			else builder.with(gap);
			builder.text(word.trim());
		}
		return builder.build();
	}

	protected final Regex pattern;

	@Override
	public IOptional<Result> match(String string) {
		final Matcher matcher = getPattern().getPattern().matcher(string);
		if (!matcher.matches()) return NullableOptional.empty();
		@SuppressWarnings("unchecked")
		final IOptional<Result> retVal = (IOptional<Result>) getPattern().getGroup().construct(matcher);
		return retVal;
	}

	@Override
	public String toString() {
		return getPattern().toString();
	}
}
