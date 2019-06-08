package com.g2forge.alexandria.adt.relationship;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CollectionRelationship<L, R, C extends Collection<R>> extends ARelationship<L, R, C> {
	protected final Supplier<C> supplier;

	public CollectionRelationship(Function<L, C> get, BiConsumer<L, C> set, BiConsumer<R, L> setRemote, Supplier<C> supplier) {
		super(get, set, setRemote);
		this.supplier = supplier;
	}

	public L add(L local, R remote) {
		C collection = get.apply(local);
		if (collection == null) set.accept(local, collection = supplier.get());
		if (!collection.contains(remote)) {
			collection.add(remote);
			if (setRemote != null) setRemote.accept(remote, local);
		}
		return local;
	}

	@Override
	protected boolean isInitialized(C field) {
		return (field != null) && !field.isEmpty();
	}

	@Override
	protected void setRemote(C field, L local) {
		if (setRemote != null) field.forEach(r -> setRemote.accept(r, local));
	}
}
