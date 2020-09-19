package com.g2forge.alexandria.regex.regex;

import java.util.Set;

import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.regex.IMatch;
import com.g2forge.alexandria.regex.IPatternBuilder;
import com.g2forge.alexandria.regex.regex.RegexPattern.Flag;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
class RegexGroupBuilder<Result, ParentBuilder> extends ARegexPatternBuilder<Result, ParentBuilder> implements IPatternBuilder<Set<Flag>, Result, RegexPattern<?>, ParentBuilder> {
	protected final ParentBuilder parent;

	protected RegexGroupBuilder(State builder, ParentBuilder parent) {
		super(builder);
		this.parent = parent;
	}

	@Override
	public ParentBuilder build(IFunction1<? super IMatch<Result>, Result> input) {
		((ARegexPatternBuilder<?, ?>) parent).active = true;
		this.active = false;
		getState().endGroup(input);
		return parent;
	}

	@Override
	protected RegexGroupBuilder<Result, ParentBuilder> getThis() {
		return this;
	}
}