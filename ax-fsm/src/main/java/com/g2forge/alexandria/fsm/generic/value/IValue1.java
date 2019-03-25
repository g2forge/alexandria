package com.g2forge.alexandria.fsm.generic.value;

import com.g2forge.alexandria.fsm.generic.type.IType1;
import com.g2forge.alexandria.java.typed.IGeneric;

public interface IValue1<G extends IGeneric<T>, T> {
	public static <G extends IGeneric<Void>> IValue1<G, Void> of(Class<G> type) {
		return new Value1<>(type, null);
	}
	
	public static <G extends IGeneric<T>, T> IValue1<G, T> of(Class<G> type, T value) {
		return new Value1<>(type, value);
	}

	public static <G extends IGeneric<G>> IValue1<G, G> of(G value) {
		return new Value1<>(value);
	}

	public static <G extends IGeneric<T>, T> IValue1<G, T> of(IType1<G, T> type, T value) {
		return new Value1<>(type, value);
	}
	
	public static <G extends IGeneric<Void>> IValue1<G, Void> of(IType1<G, Void> type) {
		return new Value1<>(type, null);
	}

	public IType1<G, T> getType();

	public T getValue();
}
