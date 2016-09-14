package com.g2forge.alexandria.data.relationship;

import java.util.function.BiConsumer;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class ARelationship<L, R, F> {
	protected final Function<L, F> get;

	protected final BiConsumer<L, F> set;

	protected final BiConsumer<R, L> setRemote;

	public L set(L local, F field) {
		final F current = get.apply(local);
		if (current != field) {
			if (isInitialized(current)) throw new IllegalStateException("Cannot change relationship once initialized");
			set.accept(local, field);
			setRemote(field, local);
		}
		return local;
	}

	protected abstract boolean isInitialized(F field);

	protected abstract void setRemote(F field, L local);
}
