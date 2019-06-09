package com.g2forge.alexandria.java.function.cache;

import java.util.function.Supplier;

import com.g2forge.alexandria.java.adt.tuple.ITuple1G_;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConcurrentFixedSupplier<T> implements Supplier<T> {
	protected static class Holder<T> implements ITuple1G_<T> {
		protected final T value;

		public Holder(final T value) {
			this.value = value;
		}

		@Override
		public T get0() {
			return value;
		}
	}

	protected final Supplier<? extends T> supplier;

	protected Holder<T> value;

	@Override
	public T get() {
		Holder<T> value = this.value;
		if (value == null) {
			synchronized (this) {
				if (this.value == null) {
					this.value = new Holder<T>(supplier.get());
				}
				value = this.value;
			}
		}
		return value.get0();
	}
}
