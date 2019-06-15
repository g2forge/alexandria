package com.g2forge.alexandria.java.adt.identity;

import java.util.Objects;

import com.g2forge.alexandria.java.core.marker.ISingleton;

/**
 * Standard implementation of {@link IIdentity} using the normal {@link Object#equals(Object)} and {@link Object#hashCode()} methods.
 *
 * @see SameIdentity
 * @see IIdentity#standard()
 */
public class StandardIdentity implements IIdentity<Object>, ISingleton {
	protected static final StandardIdentity SINGLETON = new StandardIdentity();

	public static StandardIdentity create() {
		return SINGLETON;
	}

	@Override
	public boolean equals(Object _this, Object that) {
		return Objects.equals(_this, that);
	}

	@Override
	public int hashCode(Object _this) {
		return Objects.hashCode(_this);
	}

	@Override
	public String toString() {
		return "StandardIdentity";
	}
}