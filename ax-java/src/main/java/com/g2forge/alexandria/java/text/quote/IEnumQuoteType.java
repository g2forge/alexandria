package com.g2forge.alexandria.java.text.quote;

import com.g2forge.alexandria.java.core.enums.HEnum;

public interface IEnumQuoteType<E extends Enum<E> & IEnumQuoteType<E>> extends IQuoteType {
	public default boolean isQuotedAny(final String string) {
		@SuppressWarnings("unchecked")
		final Class<E> enumClass = HEnum.getEnumClass((E) this);
		for (IQuoteType quoteType : enumClass.getEnumConstants()) {
			if (quoteType.isQuoted(string)) return true;
		}
		return false;
	}

	public default String quoteAny(final QuoteControl option, final String string) {
		@SuppressWarnings("unchecked")
		final Class<E> enumClass = HEnum.getEnumClass((E) this);
		return quote(option, string, enumClass.getEnumConstants());
	}

	public default String unquoteAny(final String string) {
		@SuppressWarnings("unchecked")
		final Class<E> enumClass = HEnum.getEnumClass((E) this);
		for (IQuoteType quoteType : enumClass.getEnumConstants()) {
			if (quoteType.isQuoted(string)) return quoteType.unquote(string);
		}
		throw new IllegalArgumentException("String \"" + string + "\" is not quoted!");
	}
}