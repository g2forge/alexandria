package com.g2forge.alexandria.java.text;

import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.LiteralFunction1;
import com.g2forge.alexandria.java.function.builder.IBuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor
@ToString
public class TextUpdateBuilder implements IConsumer1<TextUpdate<?>>, IBuilder<String> {
	protected static void verifyUpdate(TextUpdate<?> update) {
		if (update.getOffset() < 0) throw new IllegalArgumentException();
		if (update.getLength() < 0) throw new IllegalArgumentException();
		if (update.getFunction() == null) throw new NullPointerException();
	}

	protected final String input;

	protected StringBuilder retVal;

	@Override
	public void accept(TextUpdate<?> update) {
		verifyUpdate(update);
		if (retVal == null) retVal = new StringBuilder();

		final IFunction1<? super CharSequence, ? extends Object> function = update.getFunction();
		final Object result;
		if (function instanceof LiteralFunction1) result = function.apply(null);
		else result = function.apply(getInput().substring(update.getOffset(), update.getOffset() + update.getLength()));
		retVal.append(result);
	}

	@Override
	public String build() {
		if (retVal == null) return getInput();
		return retVal.toString();
	}
}