package com.g2forge.alexandria.java.identity;

import com.g2forge.alexandria.java.core.marker.ISingleton;

/**
 * Identity based on same-ness of objects. Two objects must literally be the same object (not just have similar field values or something) in memory for this
 * identity to consider them equal.
 *
 * @see StandardIdentity
 * @see IIdentity#same()
 */
public class SameIdentity implements IIdentity<Object>, ISingleton {
	protected static final SameIdentity SINGLETON = new SameIdentity();

	public static SameIdentity create() {
		return SINGLETON;
	}

	@Override
	public boolean equals(Object _this, Object that) {
		return _this == that;
	}

	@Override
	public int hashCode(Object _this) {
		return System.identityHashCode(_this);
	}

	@Override
	public String toString() {
		return "SameIdentity";
	}
}