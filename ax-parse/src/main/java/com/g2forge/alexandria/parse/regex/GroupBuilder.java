package com.g2forge.alexandria.parse.regex;

import com.g2forge.alexandria.java.fluent.optional.IOptional;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.parse.IMatch;
import com.g2forge.alexandria.parse.IMatcher;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
class GroupBuilder<Parsed, Constructed, ParentBuilder extends ARegexMatcherBuilder<?>> extends ARegexMatcherBuilder<Parsed> {
	protected final ParentBuilder parent;

	protected GroupBuilder(State builder, ParentBuilder parent) {
		super(builder);
		this.parent = parent;
	}

	@Override
	public <T> IMatcher<T, Regex> build(IFunction1<? super IMatch<Parsed>, ? extends IOptional<? extends T>> constructor) {
		parent.active = true;
		this.active = false;
		getState().endGroup(constructor);
		return null;
	}

	@Override
	protected GroupBuilder<Parsed, Constructed, ParentBuilder> getThis() {
		return this;
	}
}