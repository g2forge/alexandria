package com.g2forge.alexandria.java.type.ref;

public abstract class ATypeRefIdentity<T> implements ITypeRef<T> {
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		if (!(o instanceof ATypeRefIdentity)) return false;

		@SuppressWarnings("rawtypes")
		final ATypeRefIdentity<?> that = (ATypeRefIdentity) o;
		return getType().equals(that.getType());
	}

	@Override
	public int hashCode() {
		return getType().hashCode();
	}
}
