package com.g2forge.alexandria.fsm.transition.builder;

import com.g2forge.alexandria.fsm.HFSM;
import com.g2forge.alexandria.fsm.value.IFSMType;
import com.g2forge.alexandria.java.function.IPredicate2;
import com.g2forge.alexandria.java.type.IGeneric;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public class EventFSMTransitionBuilder<CurrentState extends IGeneric<CurrentArgument>, CurrentArgument, Event extends IGeneric<EventArgument>, EventArgument> {
	protected final IFSMType<CurrentState, CurrentArgument> current;

	protected final IFSMType<Event, EventArgument> event;

	protected IPredicate2<? super CurrentArgument, ? super EventArgument> guard;

	public EventFSMTransitionBuilder<CurrentState, CurrentArgument, Event, EventArgument> guard(IPredicate2<? super CurrentArgument, ? super EventArgument> guard) {
		this.setGuard(guard);
		return this;
	}

	public <NS extends IGeneric<NA>, NA> NextFSMTransitionBuilder<CurrentState, CurrentArgument, Event, EventArgument, NS, NA> next(Class<NS> next) {
		return next(HFSM.type(next));
	}

	public <NS extends IGeneric<NA>, NA> NextFSMTransitionBuilder<CurrentState, CurrentArgument, Event, EventArgument, NS, NA> next(IFSMType<NS, NA> next) {
		return new NextFSMTransitionBuilder<>(getCurrent(), getEvent(), getGuard(), next);
	}
}