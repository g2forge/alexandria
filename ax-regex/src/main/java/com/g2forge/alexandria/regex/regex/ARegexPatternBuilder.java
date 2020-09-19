package com.g2forge.alexandria.regex.regex;

import java.util.Set;
import java.util.regex.Pattern;

import com.g2forge.alexandria.analysis.ISerializableFunction1;
import com.g2forge.alexandria.regex.ICharacterClassBuilder;
import com.g2forge.alexandria.regex.IPatternBuilder;
import com.g2forge.alexandria.regex.NamedCharacterClass;
import com.g2forge.alexandria.regex.regex.RegexPattern.Flag;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter(AccessLevel.PROTECTED)
abstract class ARegexPatternBuilder<Result, Built> implements IPatternBuilder<Set<Flag>, Result, RegexPattern<?>, Built> {
	protected final State state;

	protected boolean active = true;

	@Override
	public IPatternBuilder<Set<Flag>, Result, RegexPattern<?>, Built> alt(RegexPattern<?> pattern0, RegexPattern<?> pattern1, RegexPattern<?>... patterns) {
		assertActive();

		final State state = getState();
		state.startGroup(null);
		state.with(pattern0);
		state.append('|');
		state.with(pattern1);
		for (RegexPattern<?> pattern : patterns) {
			state.append('|');
			state.with(pattern);
		}
		state.endGroup(null);
		return getThis();
	}

	protected void assertActive() {
		if (!active) throw new IllegalStateException("Attempt to manipulate inactive builder, perhaps you forgot to close the parent?");
	}

	@Override
	public ICharacterClassBuilder<? extends IPatternBuilder<Set<Flag>, Result, RegexPattern<?>, Built>> charClass(boolean negate) {
		assertActive();

		active = false;

		final State builder = getState();
		builder.append('[');
		if (negate) builder.append('^');

		final IPatternBuilder<Set<Flag>, Result, RegexPattern<?>, Built> retVal = getThis();
		return new ICharacterClassBuilder<IPatternBuilder<Set<Flag>, Result, RegexPattern<?>, Built>>() {
			protected boolean active = true;

			protected void assertActive() {
				if (!active) throw new IllegalStateException("Attempt to manipulate inactive builder, perhaps you forgot to close the parent?");
			}

			@Override
			public IPatternBuilder<Set<Flag>, Result, RegexPattern<?>, Built> build() {
				assertActive();
				getState().append(']');
				((ARegexPatternBuilder<?, ?>) retVal).active = true;
				active = false;
				return retVal;
			}

			@Override
			public ICharacterClassBuilder<IPatternBuilder<Set<Flag>, Result, RegexPattern<?>, Built>> character(char character) {
				assertActive();
				getState().append(character);
				return this;
			}

			@Override
			public ICharacterClassBuilder<IPatternBuilder<Set<Flag>, Result, RegexPattern<?>, Built>> named(NamedCharacterClass named) {
				assertActive();
				getState().append(named.getRegex());
				return this;
			}

			@Override
			public ICharacterClassBuilder<IPatternBuilder<Set<Flag>, Result, RegexPattern<?>, Built>> range(char start, char end) {
				assertActive();
				getState().append(start).append('-').append(end);
				return this;
			}
		};
	}

	protected abstract IPatternBuilder<Set<Flag>, Result, RegexPattern<?>, Built> getThis();

	@Override
	public <GroupResult> IPatternBuilder<Set<Flag>, GroupResult, RegexPattern<?>, ? extends IPatternBuilder<Set<Flag>, Result, RegexPattern<?>, Built>> group(ISerializableFunction1<? super Result, GroupResult> field, Set<Flag> arguments) {
		assertActive();
		if (arguments != null) throw new IllegalArgumentException("Arguments must be null!");

		final State state = getState();
		state.startGroup(field == null ? null : RegexPattern.getFieldID(field));
		active = false;
		return new RegexGroupBuilder<>(state, getThis());
	}

	@Override
	public IPatternBuilder<Set<Flag>, Result, RegexPattern<?>, Built> named(NamedCharacterClass named) {
		return pattern(named.getRegex());
	}

	@Override
	public IPatternBuilder<Set<Flag>, Result, RegexPattern<?>, Built> opt() {
		return pattern("?");
	}

	protected IPatternBuilder<Set<Flag>, Result, RegexPattern<?>, Built> pattern(final String string) {
		assertActive();
		getState().append(string);
		return getThis();
	}

	@Override
	public IPatternBuilder<Set<Flag>, Result, RegexPattern<?>, Built> plus() {
		return pattern("+");
	}

	@Override
	public IPatternBuilder<Set<Flag>, Result, RegexPattern<?>, Built> star() {
		return pattern("*");
	}

	@Override
	public IPatternBuilder<Set<Flag>, Result, RegexPattern<?>, Built> text(String text) {
		return pattern(Pattern.quote(text));
	}

	@Override
	public IPatternBuilder<Set<Flag>, Result, RegexPattern<?>, Built> with(RegexPattern<?> pattern) {
		assertActive();
		getState().with(pattern);
		return getThis();
	}
}