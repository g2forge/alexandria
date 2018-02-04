package com.g2forge.alexandria.wizard;

import com.g2forge.alexandria.java.fluent.optional.AOptional;
import com.g2forge.alexandria.java.fluent.optional.NullableOptional;

public abstract class AInput<T> extends AOptional<T> {
	@Override
	protected <U> AOptional<U> create() {
		return NullableOptional.empty();
	}

	@Override
	protected <U> AOptional<U> create(U value) {
		return NullableOptional.of(value);
	}
}
