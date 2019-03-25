package com.g2forge.alexandria.fsm.generic.type;

import com.g2forge.alexandria.java.type.IGeneric;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ClassType1<G extends IGeneric<T>, T> implements IType1<G, T> {
	public static <G extends IGeneric<T>, T> ClassType1<G, T> fromValue(T value) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final Class<G> type = (Class) value.getClass();
		return new ClassType1<>(type);
	}

	protected final Class<G> type;
}
