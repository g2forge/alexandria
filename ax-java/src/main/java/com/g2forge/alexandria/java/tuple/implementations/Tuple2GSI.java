package com.g2forge.alexandria.java.tuple.implementations;

import com.g2forge.alexandria.java.tuple.ITuple2GS;
import com.g2forge.alexandria.java.tuple.ITuple2G_;

public class Tuple2GSI<T0, T1> extends Tuple1GSI<T0> implements ITuple2GS<T0, T1> {
	protected T1 value1;

	public Tuple2GSI() {
		this(null, null);
	}

	public Tuple2GSI(final T0 value0, final T1 value1) {
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

	@Override
	public ITuple2GS<T0, T1> set0(final T0 value) {
		value0 = value;
		return this;
	}

	@Override
	public ITuple2GS<T0, T1> set1(final T1 value) {
		this.value1 = value;
		return this;
	}
}
