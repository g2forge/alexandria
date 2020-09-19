package com.g2forge.alexandria.parse.regex;

import java.util.Collection;
import java.util.regex.Pattern;

import com.g2forge.alexandria.analysis.ISerializableFunction1;
import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.parse.ICharacterClassBuilder;
import com.g2forge.alexandria.parse.IMatcher;
import com.g2forge.alexandria.parse.IMatcherBuilder;
import com.g2forge.alexandria.parse.NamedCharacterClass;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter(AccessLevel.PROTECTED)
abstract class AMatcherBuilder<Parsed> implements IMatcherBuilder<Parsed, Regex> {
	protected class CharacterClassBuilder implements ICharacterClassBuilder {
		protected boolean active = true;

		protected void assertActive() {
			if (!active) throw new IllegalStateException("Attempt to manipulate inactive builder, perhaps you forgot to close the parent?");
		}

		@Override
		public ICharacterClassBuilder character(char character) {
			assertActive();
			getState().append(character);
			return this;
		}

		@Override
		public void close() {
			assertActive();
			getState().append(']');
			getThis().active = true;
			active = false;
		}

		@Override
		public ICharacterClassBuilder named(NamedCharacterClass named) {
			assertActive();
			getState().append(named.getRegex());
			return this;
		}

		@Override
		public ICharacterClassBuilder range(char start, char end) {
			assertActive();
			getState().append(start).append('-').append(end);
			return this;
		}
	}

	protected final State state;

	protected boolean active = true;

	@Override
	public IMatcherBuilder<Parsed, Regex> alt(Collection<? extends IMatcher<?, Regex>> matchers) {
		if (matchers.size() < 2) throw new IllegalArgumentException("No point in an alt block with fewer than two alternatives!");
		assertActive();

		final State state = getState();
		state.startGroup(null);
		boolean first = true;
		for (IMatcher<?, Regex> matcher : matchers) {
			if (first) first = false;
			else state.append('|');
			state.with(matcher);
		}
		state.endGroup(null);
		return getThis();
	}

	@Override
	public IMatcherBuilder<Parsed, Regex> alt(IMatcher<?, Regex> matcher0, IMatcher<?, Regex> matcher1, @SuppressWarnings("unchecked") IMatcher<?, Regex>... matchers) {
		assertActive();

		final State state = getState();
		state.startGroup(null);
		state.with(matcher0);
		state.append('|');
		state.with(matcher1);
		for (IMatcher<?, Regex> matcher : matchers) {
			state.append('|');
			state.with(matcher);
		}
		state.endGroup(null);
		return getThis();
	}

	protected void assertActive() {
		if (!active) throw new IllegalStateException("Attempt to manipulate inactive builder, perhaps you forgot to close the parent?");
	}

	@Override
	public IMatcherBuilder<Parsed, Regex> charClass(boolean negate, IConsumer1<? super ICharacterClassBuilder> consumer) {
		assertActive();

		active = false;

		final State state = getState();
		state.append('[');
		if (negate) state.append('^');

		final CharacterClassBuilder builder = new CharacterClassBuilder();
		consumer.accept(builder);
		if (builder.active) builder.close();

		return getThis();
	}

	protected abstract AMatcherBuilder<Parsed> getThis();

	@Override
	public <_Parsed> IMatcherBuilder<Parsed, Regex> group(ISerializableFunction1<? super Parsed, _Parsed> field, IConsumer1<? super IMatcherBuilder<_Parsed, Regex>> consumer) {
		assertActive();

		final State state = getState();
		state.startGroup(field == null ? null : RegexMatcher.getFieldID(field));
		active = false;
		final GroupBuilder<_Parsed, Object, AMatcherBuilder<Parsed>> builder = new GroupBuilder<>(state, getThis());

		consumer.accept(builder);
		if (builder.isActive()) builder.build();
		return getThis();
	}

	@Override
	public IMatcherBuilder<Parsed, Regex> named(NamedCharacterClass named) {
		return pattern(named.getRegex());
	}

	@Override
	public IMatcherBuilder<Parsed, Regex> opt() {
		return pattern("?");
	}

	protected IMatcherBuilder<Parsed, Regex> pattern(final String string) {
		assertActive();
		getState().append(string);
		return getThis();
	}

	@Override
	public IMatcherBuilder<Parsed, Regex> plus() {
		return pattern("+");
	}

	@Override
	public IMatcherBuilder<Parsed, Regex> star() {
		return pattern("*");
	}

	@Override
	public IMatcherBuilder<Parsed, Regex> text(String text) {
		return pattern(Pattern.quote(text));
	}

	@Override
	public IMatcherBuilder<Parsed, Regex> with(IMatcher<?, Regex> matcher) {
		assertActive();
		getState().with(matcher);
		return getThis();
	}
}