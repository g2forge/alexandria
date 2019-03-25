package com.g2forge.alexandria.fsm;

import com.g2forge.alexandria.fsm.generic.IGeneric1;
import com.g2forge.alexandria.fsm.generic.value.IValue1;

public interface IFSM<E extends IGeneric1<?>, S extends IGeneric1<?>> {
	public void fire(IValue1<? extends E, ?> event) throws FSMDisallowedEventException;

	public IValue1<? extends S, ?> getState();
}
