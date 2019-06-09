package com.g2forge.alexandria.fsm.transition.builder;

import com.g2forge.alexandria.fsm.transition.FSMTransition;
import com.g2forge.alexandria.fsm.transition.IFSMTransition;
import com.g2forge.alexandria.fsm.value.IFSMType;
import com.g2forge.alexandria.java.function.IFunction2;
import com.g2forge.alexandria.java.function.IPredicate2;
import com.g2forge.alexandria.java.type.IGeneric;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public class EmitFSMTransitionBuilder<CurrentState extends IGeneric<CurrentArgument>, CurrentArgument, Event extends IGeneric<EventArgument>, EventArgument, NextState extends IGeneric<NextArgument>, NextArgument, Emission> {
	protected final IFSMType<CurrentState, CurrentArgument> current;

	protected final IFSMType<Event, EventArgument> event;

	protected final IPredicate2<? super CurrentArgument, ? super EventArgument> guard;

	protected final IFSMType<NextState, NextArgument> next;

	protected final IFunction2<? super CurrentArgument, ? super EventArgument, ? extends NextArgument> argument;

	protected final IFunction2<? super CurrentArgument, ? super EventArgument, ? extends Emission> emit;

	public IFSMTransition<CurrentState, CurrentArgument, Event, EventArgument, NextState, NextArgument, Emission> build() {
		return new FSMTransition<>(getCurrent(), getEvent(), getGuard(), getNext(), getArgument(), getEmit());
	}
}