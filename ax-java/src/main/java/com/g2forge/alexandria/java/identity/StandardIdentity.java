package com.g2forge.alexandria.java.identity;

import java.util.Objects;

import com.g2forge.alexandria.java.core.iface.ISingleton;

public class StandardIdentity implements IIdentity<Object>, ISingleton {
	protected static final SameIdentity SINGLETON = new SameIdentity();

	public static SameIdentity create() {
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