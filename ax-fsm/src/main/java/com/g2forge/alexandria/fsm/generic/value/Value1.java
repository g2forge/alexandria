package com.g2forge.alexandria.fsm.generic.value;

import com.g2forge.alexandria.fsm.generic.type.ClassType1;
import com.g2forge.alexandria.fsm.generic.type.IType1;
import com.g2forge.alexandria.java.typed.IGeneric;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Value1<G extends IGeneric<T>, T> implements IValue1<G, T> {
	protected final IType1<G, T> type;

	protected final T value;

	public Value1(Class<G> type, T value) {
		this(new ClassType1<>(type), value);
	}

	public Value1(T value) {
		this(ClassType1.fromValue(value), value);
	}
}
