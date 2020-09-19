package com.g2forge.alexandria.regex;

import com.g2forge.alexandria.analysis.ISerializableFunction1;

public interface IMatch<Result> {
	public <T> T getAsObject(ISerializableFunction1<? super Result, T> field);

	public String getAsString();

	public String getAsString(ISerializableFunction1<? super Result, ?> field);

	public default boolean isMatch() {
		return getAsString() != null;
	}
}
