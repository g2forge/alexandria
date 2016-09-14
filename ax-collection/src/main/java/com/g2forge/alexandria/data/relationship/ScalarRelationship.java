package com.g2forge.alexandria.data.relationship;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class ScalarRelationship<L, R> extends ARelationship<L, R, R> {
	public ScalarRelationship(Function<L, R> get, BiConsumer<L, R> set, BiConsumer<R, L> setRemote) {
		super(get, set, setRemote);
	}

	@Override
	protected boolean isInitialized(R field) {
		return field != null;
	}

	@Override
	protected void setRemote(R field, L local) {
		setRemote.accept(field, local);
	}
}
