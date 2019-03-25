package com.g2forge.alexandria.fsm.transition.builder;

import com.g2forge.alexandria.fsm.transition.FSMTransition;
import com.g2forge.alexandria.fsm.transition.IFSMTransition;
import com.g2forge.alexandria.fsm.value.IFSMType;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction2;
import com.g2forge.alexandria.java.function.IPredicate2;
import com.g2forge.alexandria.java.type.IGeneric;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
public class NextFSMTransitionBuilder<CurrentState extends IGeneric<CurrentArgument>, CurrentArgument, Event extends IGeneric<EventArgument>, EventArgument, NextState extends IGeneric<NextArgument>, NextArgument> {
	protected final IFSMType<CurrentState, CurrentArgument> current;

	protected final IFSMType<Event, EventArgument> event;

	protected final IPredicate2<? super CurrentArgument, ? super EventArgument> guard;

	protected final IFSMType<NextState, NextArgument> next;

	public IFSMTransition<CurrentState, CurrentArgument, Event, EventArgument, NextState, NextArgument> build() {
		return function();
	}

	public IFSMTransition<CurrentState, CurrentArgument, Event, EventArgument, NextState, NextArgument> function() {
		return function((IFunction2<? super CurrentArgument, ? super EventArgument, ? extends NextArgument>) null);
	}

	public IFSMTransition<CurrentState, CurrentArgument, Event, EventArgument, NextState, NextArgument> function(final IConsumer2<? super CurrentArgument, ? super EventArgument> consumer) {
		return function(consumer.toFunction(null));
	}

	public IFSMTransition<CurrentState, CurrentArgument, Event, EventArgument, NextState, NextArgument> function(IFunction2<? super CurrentArgument, ? super EventArgument, ? extends NextArgument> function) {
		return new FSMTransition<>(getCurrent(), getEvent(), getGuard(), getNext(), function);
	}
}