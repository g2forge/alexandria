package com.g2forge.alexandria.parse;

import java.util.Collection;

import com.g2forge.alexandria.analysis.ISerializableFunction1;
import com.g2forge.alexandria.java.fluent.optional.IOptional;
import com.g2forge.alexandria.java.fluent.optional.NullableOptional;
import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.function.IFunction1;

public interface IMatcherBuilder<Parsed, Pattern extends IPattern> {
	public IMatcherBuilder<Parsed, Pattern> alt(Collection<? extends IMatcher<?, Pattern>> matchers);

	@SuppressWarnings("unchecked")
	public default IMatcherBuilder<Parsed, Pattern> alt(IMatcher<?, Pattern> matcher0, IMatcher<?, Pattern> matcher1) {
		return alt(matcher0, matcher1, new IMatcher[0]);
	}

	public IMatcherBuilder<Parsed, Pattern> alt(IMatcher<?, Pattern> matcher0, IMatcher<?, Pattern> matcher1, @SuppressWarnings("unchecked") IMatcher<?, Pattern>... matchers);

	public default <T> IMatcher<T, Pattern> build() {
		return buildReq(null);
	}

	public <T> IMatcher<T, Pattern> build(IFunction1<? super IMatch<Parsed>, ? extends IOptional<? extends T>> constructor);

	public default <T> IMatcher<T, Pattern> buildFlag(T value) {
		final NullableOptional<T> optional = NullableOptional.of(value);
		return build(match -> match.isMatch() ? optional : NullableOptional.empty());
	}

	public default <T> IMatcher<T, Pattern> buildReq(IFunction1<? super IMatch<Parsed>, ? extends T> constructor) {
		return build(constructor == null ? null : match -> NullableOptional.of(constructor.apply(match)));
	}

	public default <T> IMatcher<T, Pattern> buildString(IFunction1<? super String, ? extends T> constructor) {
		return buildReq(match -> constructor.apply(match.getAsString()));
	}

	public IMatcherBuilder<Parsed, Pattern> charClass(boolean negate, IConsumer1<? super ICharacterClassBuilder> consumer);

	public default IMatcherBuilder<Parsed, Pattern> digit(int base) {
		if (base < 1 || base > 10) throw new IllegalArgumentException("We currently only support bases up to 10!");
		return charClass(false, cc -> cc.range('0', (char) ('0' + (base - 1))));
	}

	public default IMatcherBuilder<Parsed, Pattern> group(IConsumer1<? super IMatcherBuilder<Parsed, Pattern>> group) {
		return group(null, group);
	}

	public <_Parsed> IMatcherBuilder<Parsed, Pattern> group(ISerializableFunction1<? super Parsed, _Parsed> field, IConsumer1<? super IMatcherBuilder<_Parsed, Pattern>> consumer);

	public IMatcherBuilder<Parsed, Pattern> named(NamedCharacterClass named);

	public IMatcherBuilder<Parsed, Pattern> opt();

	public IMatcherBuilder<Parsed, Pattern> plus();

	public IMatcherBuilder<Parsed, Pattern> star();

	public IMatcherBuilder<Parsed, Pattern> text(String text);

	public IMatcherBuilder<Parsed, Pattern> with(IMatcher<?, Pattern> matcher);

	public default <_Parsed> IMatcherBuilder<Parsed, Pattern> with(ISerializableFunction1<? super Parsed, _Parsed> field, IMatcher<_Parsed, Pattern> matcher) {
		return group(field, (IMatcherBuilder<_Parsed, Pattern> g) -> g.with(matcher));
	}
}
