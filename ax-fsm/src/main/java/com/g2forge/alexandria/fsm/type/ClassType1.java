package com.g2forge.alexandria.fsm.type;

import com.g2forge.alexandria.fsm.generic.IGeneric1;

import lombok.Data;

@Data
public class ClassType1<G extends IGeneric1<T>, T> implements IType1<G, T> {
	protected final Class<G> type;
}
