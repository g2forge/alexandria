package com.g2forge.alexandria.fsm.value;

import com.g2forge.alexandria.java.type.IGeneric;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ClassFSMType<G extends IGeneric<T>, T> implements IFSMType<G, T> {
	protected final Class<G> type;
}
