package com.g2forge.alexandria.java.identity;

public interface IIdentity<T> {
	public static IIdentity<Object> same() {
		return SameIdentity.create();
	}

	public static IIdentity<Object> standard() {
		return StandardIdentity.create();
	}

	public boolean equals(T _this, Object that);

	public int hashCode(T _this);

	public default Identified<T> of(T value) {
		return new Identified<>(value, this);
	}
}
