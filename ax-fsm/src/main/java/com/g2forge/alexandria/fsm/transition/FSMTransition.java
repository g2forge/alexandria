package com.g2forge.alexandria.fsm.transition;

import com.g2forge.alexandria.fsm.value.IFSMType;
import com.g2forge.alexandria.java.function.IFunction2;
import com.g2forge.alexandria.java.function.IPredicate2;
import com.g2forge.alexandria.java.type.IGeneric;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @param <CurrentState> The type of current transition state.
 * @param <CurrentArgument> The type of the current transition state argument.
 * @param <Event> The type of the transition event.
 * @param <EventArgument> The type of the transition event argument.
 * @param <NextState> The type of the next transition state.
 * @param <NextArgument> The type of the next transition state argument.
 */
@Data
@Builder
@AllArgsConstructor
public class FSMTransition<CurrentState extends IGeneric<CurrentArgument>, CurrentArgument, Event extends IGeneric<EventArgument>, EventArgument, NextState extends IGeneric<NextArgument>, NextArgument, Output> implements IFSMTransition<CurrentState, CurrentArgument, Event, EventArgument, NextState, NextArgument, Output> {
	protected final IFSMType<CurrentState, CurrentArgument> current;

	protected final IFSMType<Event, EventArgument> event;

	protected final IPredicate2<? super CurrentArgument, ? super EventArgument> guard;

	protected final IFSMType<NextState, NextArgument> next;

	protected final IFunction2<? super CurrentArgument, ? super EventArgument, ? extends NextArgument> argument;

	protected final IFunction2<? super CurrentArgument, ? super EventArgument, ? extends Output> output;
}
