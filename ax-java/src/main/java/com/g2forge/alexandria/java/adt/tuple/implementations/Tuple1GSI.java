package com.g2forge.alexandria.java.adt.tuple.implementations;

import com.g2forge.alexandria.java.adt.tuple.ITuple1GS;
import com.g2forge.alexandria.java.adt.tuple.ITuple1G_;

public class Tuple1GSI<T0> implements ITuple1GS<T0> {
	protected T0 value0;

	public Tuple1GSI() {
		this(null);
	}

	public Tuple1GSI(final T0 value0) {
		this.value0 = value0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;

		final ITuple1G_<?> that = (ITuple1G_<?>) obj;
		return get0() == that.get0();
	}

	@Override
	public T0 get0() {
		return value0;
	}

	@Override
	public int hashCode() {
		return System.identityHashCode(get0());
	}

	@Override
	public ITuple1GS<T0> set0(final T0 value) {
		this.value0 = value;
		return this;
	}
}
