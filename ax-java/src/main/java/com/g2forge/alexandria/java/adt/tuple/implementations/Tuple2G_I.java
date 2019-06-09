package com.g2forge.alexandria.java.adt.tuple.implementations;

import com.g2forge.alexandria.java.adt.tuple.ITuple2G_;

public class Tuple2G_I<T0, T1> extends Tuple1G_I<T0> implements ITuple2G_<T0, T1> {
	protected final T1 value1;

	public Tuple2G_I(final T0 value0, final T1 value1) {
		super(value0);
		this.value1 = value1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;

		final ITuple2G_<?, ?> that = (ITuple2G_<?, ?>) obj;
		return (get0() == that.get0()) && (get1() == that.get1());
	}

	@Override
	public T1 get1() {
		return value1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + System.identityHashCode(get0());
		result = prime * result + System.identityHashCode(get1());
		return result;
	}
}
