package com.g2forge.alexandria.fsm.transition.builder;

import com.g2forge.alexandria.fsm.generic.type.IType1;
import com.g2forge.alexandria.fsm.transition.ITransition;
import com.g2forge.alexandria.fsm.transition.Transition;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction2;
import com.g2forge.alexandria.java.function.IPredicate2;
import com.g2forge.alexandria.java.type.IGeneric;

import lombok.Data;

@Data
public class NextTransitionBuilder<CurrentState extends IGeneric<CurrentArgument>, CurrentArgument, Event extends IGeneric<EventArgument>, EventArgument, NextState extends IGeneric<NextArgument>, NextArgument> {
	protected final IType1<CurrentState, CurrentArgument> current;

	protected final IType1<Event, EventArgument> event;

	protected final IPredicate2<? super CurrentArgument, ? super EventArgument> guard;

	protected final IType1<NextState, NextArgument> next;

	public ITransition<CurrentState, CurrentArgument, Event, EventArgument, NextState, NextArgument> function() {
		return function((IFunction2<? super CurrentArgument, ? super EventArgument, ? extends NextArgument>) null);
	}

	public ITransition<CurrentState, CurrentArgument, Event, EventArgument, NextState, NextArgument> function(final IConsumer2<? super CurrentArgument, ? super EventArgument> consumer) {
		return function((ca, ea) -> {
			consumer.accept(ca, ea);
			return null;
		});
	}

	public ITransition<CurrentState, CurrentArgument, Event, EventArgument, NextState, NextArgument> function(IFunction2<? super CurrentArgument, ? super EventArgument, ? extends NextArgument> function) {
		return new Transition<>(getCurrent(), getEvent(), getGuard(), getNext(), function);
	}
}