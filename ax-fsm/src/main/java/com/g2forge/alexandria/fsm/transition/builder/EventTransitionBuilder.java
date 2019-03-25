package com.g2forge.alexandria.fsm.transition.builder;

import com.g2forge.alexandria.fsm.generic.type.ClassType1;
import com.g2forge.alexandria.fsm.generic.type.IType1;
import com.g2forge.alexandria.java.function.IPredicate2;
import com.g2forge.alexandria.java.typed.IGeneric;

import lombok.Data;

@Data
public class EventTransitionBuilder<CurrentState extends IGeneric<CurrentArgument>, CurrentArgument, Event extends IGeneric<EventArgument>, EventArgument> {
	protected final IType1<CurrentState, CurrentArgument> current;

	protected final IType1<Event, EventArgument> event;

	protected IPredicate2<? super CurrentArgument, ? super EventArgument> guard;

	public EventTransitionBuilder<CurrentState, CurrentArgument, Event, EventArgument> guard(IPredicate2<? super CurrentArgument, ? super EventArgument> guard) {
		this.guard = guard;
		return this;
	}

	public <NS extends IGeneric<NA>, NA> NextTransitionBuilder<CurrentState, CurrentArgument, Event, EventArgument, NS, NA> next(Class<NS> next) {
		return next(new ClassType1<>(next));
	}

	public <NS extends IGeneric<NA>, NA> NextTransitionBuilder<CurrentState, CurrentArgument, Event, EventArgument, NS, NA> next(IType1<NS, NA> next) {
		return new NextTransitionBuilder<>(getCurrent(), getEvent(), getGuard(), next);
	}
}