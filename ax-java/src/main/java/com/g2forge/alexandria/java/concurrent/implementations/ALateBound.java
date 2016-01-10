package com.g2forge.alexandria.java.concurrent.implementations;

import com.g2forge.alexandria.java.tuple.ITuple1G_;

public abstract class ALateBound<T> implements ITuple1G_<T> {
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
	
	protected Holder<T> value;
	
	protected abstract T compute();
	
	@Override
	public T get0() {
		Holder<T> value = this.value;
		if (value == null) {
			synchronized (this) {
				if (this.value == null) {
					this.value = new Holder<T>(compute());
				}
				value = this.value;
			}
		}
		return value.get0();
	}
}
