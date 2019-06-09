package com.g2forge.alexandria.fsm.value;

import com.g2forge.alexandria.java.type.IGeneric;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FSMValue<G extends IGeneric<T>, T> implements IFSMValue<G, T> {
	protected final IFSMType<G, T> type;

	protected final T value;
}
