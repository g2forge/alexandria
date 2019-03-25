package com.g2forge.alexandria.fsm.value;

import com.g2forge.alexandria.java.type.IGeneric;
import com.g2forge.alexandria.java.type.ITyped;

public interface IFSMValue<G extends IGeneric<T>, T> extends ITyped<IFSMType<G, T>> {
	public T getValue();
}
