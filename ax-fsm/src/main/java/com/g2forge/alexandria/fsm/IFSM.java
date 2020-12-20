package com.g2forge.alexandria.fsm;

import com.g2forge.alexandria.fsm.value.IFSMValue;

/**
 * A finite state machine. Instances of this interfaces can be built with {@link FSMBuilder}.
 * 
 * @param <Event> The root event type of this FSM.
 * @param <State> The root state type of this FSM.
 * @param <Emission> the type of values emitted on FSM transitions.
 */
public interface IFSM<Event , State , Emission, Output> {
	/**
	 * Fire an event, which may change the state of this FSM.
	 * 
	 * @param event The event to fire
	 * @return A mealy-style output, or &quot;emission&quot; from this event firing. If you wish to implement a moore machine, you should use
	 *         {@link #getOutput()}.
	 * @throws FSMDisallowedEventException If the event is disallowed in the {@link #getState() current state}.
	 */
	public Emission fire(IFSMValue<? extends Event, ?> event) throws FSMDisallowedEventException;

	public Output getOutput();

	public IFSMValue<? extends State, ?> getState();
}
