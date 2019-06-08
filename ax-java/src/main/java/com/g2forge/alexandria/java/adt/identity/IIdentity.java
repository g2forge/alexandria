package com.g2forge.alexandria.java.adt.identity;

/**
 * Abstract encapsulation of the concept of a type's identity. This is particularly useful when you want to use objects of that type in a map or collection, but
 * you want to define the {@link #equals(Object, Object)} and {@link #hashCode(Object)} objects in a way that differs from the type's default. Objects returned
 * from {@link #of(Object)} will wrap values of the specified type <code>T</code> and use the identity methods from this {@link IIdentity}.
 */
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
