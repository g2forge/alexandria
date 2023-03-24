package com.g2forge.alexandria.java.text.casing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.g2forge.alexandria.java.function.ISupplier;

public abstract class ACase implements ICase {
	protected abstract String convertToken(CasedToken token);

	@Override
	public ICasedText fromString(String string) {
		return new ICasedText() {
			@Override
			public ISupplier<CasedToken> get() {
				return new ISupplier<CasedToken>() {
					protected final Matcher matcher = getTokenPattern().matcher(string);

					protected int position = 0;

					@Override
					public CasedToken get() {
						if (position == string.length()) return null;
						if (!matcher.find()) throw new IllegalArgumentException();
						if (matcher.start() != position) throw new IllegalArgumentException();
						position = matcher.end();
						final String string = matcher.group(1);
						return new CasedToken(getType(string), string);
					}

					@Override
					public String toString() {
						return String.format("%1$s.ICasedText.Supplier(%2$s @ %3$d)", ACase.this.getClass().getSimpleName(), string, position);
					}
				};
			}

			@Override
			public String toString() {
				return String.format("%1$s.ICasedText(%2$s)", ACase.this.getClass().getSimpleName(), string);
			}
		};
	}

	protected abstract Pattern getTokenPattern();

	protected abstract String getTokenSeparator();

	protected abstract CasedToken.Type getType(String string);

	@Override
	public String toString(ICasedText text) {
		final StringBuilder retVal = new StringBuilder();
		final String separator = getTokenSeparator();
		final ISupplier<CasedToken> supplier = text.get();
		while (true) {
			try {
				final CasedToken token = supplier.get();
				if (token == null) break;
				if ((retVal.length() > 0) && (separator != null)) retVal.append(separator);
				retVal.append(convertToken(token));
			} catch (Throwable throwable) {
				throw new RuntimeException(String.format("Could not convert \"%1$s\" to %2$s, failed after \"%3$s\"!", text, this, retVal), throwable);
			}
		}
		return retVal.toString();
	}
}
