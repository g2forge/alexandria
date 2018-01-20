package com.g2forge.alexandria.java.tuple.implementations;

import java.util.Objects;

import com.g2forge.alexandria.java.tuple.ITuple2G_;

public class Tuple2G_O<T0, T1> extends Tuple1G_O<T0> implements ITuple2G_<T0, T1> {
	protected final T1 value1;

	public Tuple2G_O(final T0 value0, final T1 value1) {
		super(value0);
		this.value1 = value1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;

		final Tuple2G_O<?, ?> that = (Tuple2G_O<?, ?>) obj;
		return Objects.equals(get0(), that.get0()) && Objects.equals(get1(), that.get1());
	}

	@Override
	public T1 get1() {
		return value1;
	}

	@Override
	public int hashCode() {
		return Objects.hash(get0(), get1());
	}
}
