package com.g2forge.alexandria.java.tuple.implementations;

import java.util.Objects;

import com.g2forge.alexandria.java.tuple.ITuple1G_;

public class Tuple1G_O<T0> implements ITuple1G_<T0> {
	protected final T0 value0;

	public Tuple1G_O(final T0 value0) {
		this.value0 = value0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;

		final ITuple1G_<?> that = (ITuple1G_<?>) obj;
		return Objects.equals(get0(), that.get0());
	}

	@Override
	public T0 get0() {
		return value0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(get0());
	}
}
