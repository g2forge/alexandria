package com.g2forge.alexandria.fsm.transition.builder;

import com.g2forge.alexandria.fsm.HFSM;
import com.g2forge.alexandria.fsm.value.IFSMType;
import com.g2forge.alexandria.java.type.IGeneric;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
public class CurrentFSMTransitionBuilder<CurrentState extends IGeneric<CurrentArgument>, CurrentArgument> {
	protected final IFSMType<CurrentState, CurrentArgument> current;

	public <Event extends IGeneric<EventArgument>, EventArgument> EventFSMTransitionBuilder<CurrentState, CurrentArgument, Event, EventArgument> event(Class<Event> event) {
		return event(HFSM.type(event));
	}

	public <Event extends IGeneric<EventArgument>, EventArgument> EventFSMTransitionBuilder<CurrentState, CurrentArgument, Event, EventArgument> event(IFSMType<Event, EventArgument> event) {
		return new EventFSMTransitionBuilder<>(getCurrent(), event);
	}
}