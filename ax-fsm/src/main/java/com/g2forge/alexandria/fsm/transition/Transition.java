package com.g2forge.alexandria.fsm.transition;

import com.g2forge.alexandria.fsm.generic.type.IType1;
import com.g2forge.alexandria.java.function.IFunction2;
import com.g2forge.alexandria.java.function.IPredicate2;
import com.g2forge.alexandria.java.typed.IGeneric;

import lombok.Data;

@Data
public class Transition<CurrentState extends IGeneric<CurrentArgument>, CurrentArgument, Event extends IGeneric<EventArgument>, EventArgument, NextState extends IGeneric<NextArgument>, NextArgument> implements ITransition<CurrentState, CurrentArgument, Event, EventArgument, NextState, NextArgument> {
	protected final IType1<CurrentState, CurrentArgument> current;

	protected final IType1<Event, EventArgument> event;

	protected final IPredicate2<? super CurrentArgument, ? super EventArgument> guard;

	protected final IType1<NextState, NextArgument> next;

	protected final IFunction2<? super CurrentArgument, ? super EventArgument, ? extends NextArgument> function;
}
