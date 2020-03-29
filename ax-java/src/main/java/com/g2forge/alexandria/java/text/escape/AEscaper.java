package com.g2forge.alexandria.java.text.escape;

import java.util.regex.Pattern;

import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.text.TextUpdate;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter(AccessLevel.PROTECTED)
public abstract class AEscaper implements IEscaper {
	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	protected static class Change {
		protected final int length;

		protected final IFunction1<? super CharSequence, ?> function;
	}

	protected final String prefix;

	protected final String postfix;

	@Getter(lazy = true)
	private final Pattern requiresEscapePattern = computeRequiresEscapePattern();

	/**
	 * Create an escaper.
	 * 
	 * @param prefix A string which prefixes all escape sequences.
	 * @param postfix A string which postfixes all escape sequences.
	 */
	public AEscaper(String prefix, String postfix) {
		this.prefix = prefix;
		this.postfix = postfix;
	}

	public void computeEscape(String string, final IConsumer1<? super TextUpdate<?>> consumer) {
		final IFunction1<CharSequence, String> prefix = getPrefix() == null ? null : IFunction1.create(getPrefix());
		final IFunction1<CharSequence, String> postfix = getPostfix() == null ? null : IFunction1.create(getPostfix());

		int startCopy = -1;
		for (int i = 0; i < string.length(); i++) {
			final Change change = computeEscape(string, i);
			if (change != null) {
				if (startCopy >= 0) consumer.accept(new TextUpdate<>(startCopy, i - startCopy, IFunction1.identity()));
				startCopy = -1;

				if (prefix != null) consumer.accept(new TextUpdate<>(i, 0, prefix));
				consumer.accept(new TextUpdate<Object>(i, change.getLength(), change.getFunction()));
				i += change.getLength() - 1;
				if (postfix != null) consumer.accept(new TextUpdate<>(i, 0, postfix));
			} else if (startCopy < 0) startCopy = i;
		}
		if (startCopy >= 0) consumer.accept(new TextUpdate<>(startCopy, string.length() - startCopy, IFunction1.identity()));
	}

	protected abstract Change computeEscape(String string, int index);

	protected abstract Pattern computeRequiresEscapePattern();

	public void computeUnescape(String string, final IConsumer1<? super TextUpdate<?>> consumer) {
		final String prefix = getPrefix(), postfix = getPostfix();
		final int prefixLength = prefix == null ? 0 : prefix.length(), postfixLength = postfix == null ? 0 : postfix.length();

		int startCopy = -1;
		for (int i = 0; i < string.length(); i++) {
			if ((prefix == null) || string.startsWith(prefix, i)) {
				final Change change = computeUnescape(string, i + prefixLength);
				if (change != null) {
					if ((postfix == null) || string.startsWith(postfix, i + prefixLength + change.getLength())) {
						if (startCopy >= 0) consumer.accept(new TextUpdate<>(startCopy, i - startCopy, IFunction1.identity()));
						startCopy = -1;

						consumer.accept(new TextUpdate<Object>(i + prefixLength, change.getLength(), change.getFunction()));
						i += prefixLength + postfixLength + change.getLength() - 1 /* -1 to account for the i++ on the loop */;
						continue;
					}
				}
			}
			if (startCopy < 0) startCopy = i;
		}
		if (startCopy >= 0) consumer.accept(new TextUpdate<>(startCopy, string.length() - startCopy, IFunction1.identity()));
	}

	protected abstract Change computeUnescape(String string, int index);
}