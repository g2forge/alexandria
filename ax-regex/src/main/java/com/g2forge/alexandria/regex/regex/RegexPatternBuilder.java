package com.g2forge.alexandria.regex.regex;

import java.util.Set;

import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.regex.IMatch;
import com.g2forge.alexandria.regex.IPatternBuilder;
import com.g2forge.alexandria.regex.regex.RegexPattern.Flag;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
class RegexPatternBuilder<Result> extends ARegexPatternBuilder<Result, RegexPattern<Result>> implements IPatternBuilder<Set<Flag>, Result, RegexPattern<?>, RegexPattern<Result>> {
	protected final Set<Flag> flags;

	protected RegexPatternBuilder(Set<Flag> flags) {
		super(new State());
		this.flags = flags;
	}

	@Override
	public RegexPattern<Result> build(IFunction1<? super IMatch<Result>, Result> input) {
		return getState().build(getFlags(), input);
	}

	@Override
	protected RegexPatternBuilder<Result> getThis() {
		return this;
	}
}