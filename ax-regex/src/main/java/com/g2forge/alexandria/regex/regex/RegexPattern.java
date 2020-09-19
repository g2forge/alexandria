package com.g2forge.alexandria.regex.regex;

import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.g2forge.alexandria.analysis.ISerializableFunction1;
import com.g2forge.alexandria.java.fluent.optional.IOptional;
import com.g2forge.alexandria.java.fluent.optional.NullableOptional;
import com.g2forge.alexandria.regex.IPattern;
import com.g2forge.alexandria.regex.IPatternBuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RegexPattern<Result> implements IPattern<Result> {
	@Getter
	@RequiredArgsConstructor
	public enum Flag {
		CASE_INSENSITIVE(Pattern.CASE_INSENSITIVE);

		protected final int flag;
	}

	public static <T> IPatternBuilder<Set<Flag>, T, RegexPattern<?>, RegexPattern<T>> builder(Flag... flags) {
		final EnumSet<Flag> arguments = EnumSet.noneOf(Flag.class);
		for (Flag flag : flags)
			arguments.add(flag);
		return new RegexPatternBuilder<T>(arguments);
	}

	protected static Object getFieldID(ISerializableFunction1<?, ?> field) {
		return field.asMethodAnalyzer().getPath();
	}

	protected final Pattern pattern;

	protected final int nGroups;

	protected final Group<?> group;

	@Override
	public IOptional<Result> match(String string) {
		final Matcher matcher = getPattern().matcher(string);
		if (!matcher.matches()) return NullableOptional.empty();
		@SuppressWarnings("unchecked")
		final IOptional<Result> retVal = (IOptional<Result>) group.construct(matcher);
		return retVal;
	}

	@Override
	public String toString() {
		return getPattern().toString();
	}
}
