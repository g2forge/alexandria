package com.g2forge.alexandria.fsm;

import com.g2forge.alexandria.fsm.value.IFSMValue;
import com.g2forge.alexandria.java.type.IGeneric;

/**
 * A finite state machine. Instances of this interfaces can be built with {@link FSMBuilder}.
 * 
 * @param <Event> The root event type of this FSM.
 * @param <State> The root state type of this FSM.
 * @param <Emission> the type of values emitted on FSM transitions.
 */
public interface IFSM<Event extends IGeneric<?>, State extends IGeneric<?>, Emission> {
	/**
	 * TODO
	 * 
	 * @param event
	 * @return A mealy-style output, or &quot;emission&quot; from this event firing. If you wish to implement a moore machine, you should instead compute the
	 *         output from the value returned by {@link #getState()}.
	 * @throws FSMDisallowedEventException
	 */
	public Emission fire(IFSMValue<? extends Event, ?> event) throws FSMDisallowedEventException;

	public IFSMValue<? extends State, ?> getState();
}
