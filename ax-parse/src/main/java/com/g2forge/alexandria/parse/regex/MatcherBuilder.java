package com.g2forge.alexandria.parse.regex;

import java.util.Set;

import com.g2forge.alexandria.java.fluent.optional.IOptional;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.parse.IMatch;
import com.g2forge.alexandria.parse.IMatcher;
import com.g2forge.alexandria.parse.IMatcherBuilder;
import com.g2forge.alexandria.parse.regex.RegexMatcher.Flag;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
class MatcherBuilder<Parsed, Constructed> extends AMatcherBuilder<Parsed> implements IMatcherBuilder<Parsed, Regex> {
	protected final Set<Flag> flags;

	protected MatcherBuilder(Set<Flag> flags) {
		super(new State());
		this.flags = flags;
	}

	@Override
	public <T> IMatcher<T, Regex> build(IFunction1<? super IMatch<Parsed>, ? extends IOptional<? extends T>> constructor) {
		return getState().build(getFlags(), constructor);
	}

	@Override
	protected MatcherBuilder<Parsed, Constructed> getThis() {
		return this;
	}
}