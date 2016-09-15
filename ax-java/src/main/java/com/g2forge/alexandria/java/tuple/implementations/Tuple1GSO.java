package com.g2forge.alexandria.java.tuple.implementations;

import java.util.Objects;

import com.g2forge.alexandria.java.tuple.ITuple1GS;

public class Tuple1GSO<T0> implements ITuple1GS<T0> {
	protected T0 value0;

	public Tuple1GSO() {
		this(null);
	}

	/**
	 * @param value0
	 */
	public Tuple1GSO(final T0 value0) {
		this.value0 = value0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;

		final Tuple1G_O<?> that = (Tuple1G_O<?>) obj;
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

	@Override
	public ITuple1GS<T0> set0(final T0 value) {
		this.value0 = value;
		return this;
	}

	@Override
	public T0 swap0(final T0 value) {
		final T0 retVal = value0;
		set0(value);
		return retVal;
	}
}
