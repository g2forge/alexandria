package com.g2forge.alexandria.fsm;

import com.g2forge.alexandria.fsm.value.IFSMValue;
import com.g2forge.alexandria.java.type.IGeneric;

/**
 * A finite state machine. Instances of this interfaces can be built with {@link FSMBuilder}.
 * 
 * @param <Event> The root event type of this FSM.
 * @param <State> The root state type of this FSM.
 */
public interface IFSM<Event extends IGeneric<?>, State extends IGeneric<?>> {
	public void fire(IFSMValue<? extends Event, ?> event) throws FSMDisallowedEventException;

	public IFSMValue<? extends State, ?> getState();
}
