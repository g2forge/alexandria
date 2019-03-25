package com.g2forge.alexandria.fsm.transition.builder;

import com.g2forge.alexandria.fsm.generic.type.ClassType1;
import com.g2forge.alexandria.fsm.generic.type.IType1;
import com.g2forge.alexandria.java.typed.IGeneric;

import lombok.Data;

@Data
public class CurrentTransitionBuilder<CurrentState extends IGeneric<CurrentArgument>, CurrentArgument> {
	protected final IType1<CurrentState, CurrentArgument> current;

	public <Event extends IGeneric<EventArgument>, EventArgument> EventTransitionBuilder<CurrentState, CurrentArgument, Event, EventArgument> event(Class<Event> event) {
		return event(new ClassType1<Event, EventArgument>(event));
	}

	public <Event extends IGeneric<EventArgument>, EventArgument> EventTransitionBuilder<CurrentState, CurrentArgument, Event, EventArgument> event(IType1<Event, EventArgument> event) {
		return new EventTransitionBuilder<>(getCurrent(), event);
	}
}