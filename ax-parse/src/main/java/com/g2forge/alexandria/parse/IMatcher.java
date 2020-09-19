package com.g2forge.alexandria.parse;

import com.g2forge.alexandria.java.fluent.optional.IOptional;

@FunctionalInterface
public interface IMatcher<Result, Pattern extends IPattern> {
	public default Pattern getPattern() {
		throw new UnsupportedOperationException();
	}

	public IOptional<Result> match(String string);
}
