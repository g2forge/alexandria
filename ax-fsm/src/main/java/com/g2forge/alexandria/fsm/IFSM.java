package com.g2forge.alexandria.fsm;

import com.g2forge.alexandria.fsm.generic.value.IValue1;
import com.g2forge.alexandria.java.type.IGeneric;

public interface IFSM<Event extends IGeneric<?>, State extends IGeneric<?>> {
	public void fire(IValue1<? extends Event, ?> event) throws FSMDisallowedEventException;

	public IValue1<? extends State, ?> getState();
}
