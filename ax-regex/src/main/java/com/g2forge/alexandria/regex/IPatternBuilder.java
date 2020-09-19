package com.g2forge.alexandria.regex;

import com.g2forge.alexandria.analysis.ISerializableFunction1;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.builder.IBuilder;
import com.g2forge.alexandria.java.function.builder.IConvertingBuilder;

public interface IPatternBuilder<Arguments, Result, Pattern extends IPattern<?>, Built> extends IConvertingBuilder<IFunction1<? super IMatch<Result>, Result>, Built>, IBuilder<Built> {
	public IPatternBuilder<Arguments, Result, Pattern, Built> alt(Pattern pattern0, Pattern pattern1, @SuppressWarnings("unchecked") Pattern... patterns);

	@Override
	public default Built build() {
		return build(null);
	}

	public default ICharacterClassBuilder<? extends IPatternBuilder<Arguments, Result, Pattern, Built>> charClass() {
		return charClass(false);
	}

	public ICharacterClassBuilder<? extends IPatternBuilder<Arguments, Result, Pattern, Built>> charClass(boolean negate);

	public default IPatternBuilder<Arguments, Result, Pattern, Built> digit(int base) {
		if (base < 1 || base > 10) throw new IllegalArgumentException("We currently only support bases up to 10!");
		return charClass().range('0', (char) ('0' + (base - 1))).build();
	}

	public default IPatternBuilder<Arguments, Result, Pattern, ? extends IPatternBuilder<Arguments, Result, Pattern, Built>> group() {
		return group(null, null);
	}

	public <GroupResult> IPatternBuilder<Arguments, GroupResult, Pattern, ? extends IPatternBuilder<Arguments, Result, Pattern, Built>> group(ISerializableFunction1<? super Result, GroupResult> field, Arguments arguments);

	public IPatternBuilder<Arguments, Result, Pattern, Built> named(NamedCharacterClass named);

	public IPatternBuilder<Arguments, Result, Pattern, Built> opt();

	public IPatternBuilder<Arguments, Result, Pattern, Built> plus();

	public IPatternBuilder<Arguments, Result, Pattern, Built> star();

	public IPatternBuilder<Arguments, Result, Pattern, Built> text(String text);

	public IPatternBuilder<Arguments, Result, Pattern, Built> with(Pattern pattern);
}
