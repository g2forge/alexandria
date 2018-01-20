package com.g2forge.alexandria.java.identity;

import com.g2forge.alexandria.java.core.helpers.HObject;
import com.g2forge.alexandria.java.function.LiteralSupplier;

import lombok.Getter;

/**
 * A wrapper for an object which uses an externally specified {@link IIdentity} to determine object identity.
 */
public class Identified<T> extends LiteralSupplier<T> {
	@Getter
	protected final IIdentity<T> identity;

	public Identified(T value, IIdentity<T> identity) {
		super(value);
		this.identity = identity;
	}

	@Override
	public boolean equals(Object that) {
		if (that == null) return false;
		if (this == that) return true;
		if (!(that instanceof Identified)) return false;

		final Identified<?> cast = (Identified<?>) that;
		return getIdentity().equals(cast.getIdentity()) && getIdentity().equals(get(), cast.get());
	}

	@Override
	public int hashCode() {
		return getIdentity().hashCode(get());
	}

	@Override
	public String toString() {
		return HObject.toString(this, Identified::getIdentity, Identified::get);
	}
}
