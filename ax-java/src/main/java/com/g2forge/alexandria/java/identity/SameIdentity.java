package com.g2forge.alexandria.java.identity;

import com.g2forge.alexandria.java.core.iface.ISingleton;

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