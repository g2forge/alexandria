package com.g2forge.alexandria.regex;

import com.g2forge.alexandria.java.fluent.optional.IOptional;

public interface IPattern<Result> {
	public IOptional<Result> match(String string);
}
