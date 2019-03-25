package com.g2forge.alexandria.fsm.value;

import com.g2forge.alexandria.fsm.generic.IGeneric1;
import com.g2forge.alexandria.fsm.type.ClassType1;
import com.g2forge.alexandria.fsm.type.IType1;

import lombok.Data;

@Data
public class Value1<G extends IGeneric1<T>, T> implements IValue1<G, T> {
	protected final IType1<G, T> type;

	protected final T value;

	public Value1(Class<G> type, T value) {
		this(new ClassType1<>(type), value);
	}

	public Value1(IType1<G, T> type, T value) {
		this.type = type;
		this.value = value;
	}
	
	public Value1(T value) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final Class<G> type = (Class) value.getClass();
		this.type = new ClassType1<>(type);
		this.value = value;
	}
}
