package com.g2forge.alexandria.fsm.value;

import com.g2forge.alexandria.java.type.IGeneric;
import com.g2forge.alexandria.java.type.ITyped;

public interface IFSMValue<G extends IGeneric<T>, T> extends ITyped<IFSMType<G, T>> {
	public T getValue();

	@SuppressWarnings("unchecked")
	public default <_G extends IGeneric<_T>, _T> _T getValue(IFSMType<_G, _T> type) {
		if (!getType().equals(type)) throw new IllegalArgumentException();
		return (_T) getValue();
	}
}
