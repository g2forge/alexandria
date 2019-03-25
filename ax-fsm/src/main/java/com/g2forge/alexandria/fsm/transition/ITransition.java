package com.g2forge.alexandria.fsm.transition;

import com.g2forge.alexandria.fsm.generic.type.ClassType1;
import com.g2forge.alexandria.fsm.generic.type.IType1;
import com.g2forge.alexandria.fsm.transition.builder.CurrentTransitionBuilder;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction2;
import com.g2forge.alexandria.java.function.IPredicate2;
import com.g2forge.alexandria.java.type.IGeneric;

public interface ITransition<CurrentState extends IGeneric<CurrentArgument>, CurrentArgument, Event extends IGeneric<EventArgument>, EventArgument, NextState extends IGeneric<NextArgument>, NextArgument> {
	public static <CurrentState extends IGeneric<CurrentArgument>, CurrentArgument> CurrentTransitionBuilder<CurrentState, CurrentArgument> of(Class<CurrentState> current) {
		return of(new ClassType1<>(current));
	}

	public static <CurrentState extends IGeneric<CurrentArgument>, CurrentArgument> CurrentTransitionBuilder<CurrentState, CurrentArgument> of(IType1<CurrentState, CurrentArgument> current) {
		return new CurrentTransitionBuilder<>(current);
	}

	public static <CurrentState extends IGeneric<CurrentArgument>, CurrentArgument, Event extends IGeneric<EventArgument>, EventArgument, NextState extends IGeneric<NextArgument>, NextArgument> ITransition<CurrentState, CurrentArgument, Event, EventArgument, NextState, NextArgument> of(IType1<CurrentState, CurrentArgument> current, IType1<Event, EventArgument> event, IType1<NextState, NextArgument> next, IFunction2<? super CurrentArgument, ? super EventArgument, ? extends NextArgument> function) {
		return new Transition<>(current, event, null, next, function);
	}

	public static <CurrentState extends IGeneric<CurrentArgument>, CurrentArgument, Event extends IGeneric<EventArgument>, EventArgument, NextState extends IGeneric<Void>> ITransition<CurrentState, CurrentArgument, Event, EventArgument, NextState, Void> of(IType1<CurrentState, CurrentArgument> current, IType1<Event, EventArgument> event, IType1<NextState, Void> next) {
		return ITransition.of(current, event, next, (ca, ea) -> {
			return null;
		});
	}

	public static <CurrentState extends IGeneric<CurrentArgument>, CurrentArgument, Event extends IGeneric<EventArgument>, EventArgument, NextState extends IGeneric<Void>> ITransition<CurrentState, CurrentArgument, Event, EventArgument, NextState, Void> of(IType1<CurrentState, CurrentArgument> current, IType1<Event, EventArgument> event, IType1<NextState, Void> next, IConsumer2<? super CurrentArgument, ? super EventArgument> function) {
		return ITransition.of(current, event, next, (ca, ea) -> {
			function.accept(ca, ea);
			return null;
		});
	}

	public IType1<CurrentState, CurrentArgument> getCurrent();

	public IType1<Event, EventArgument> getEvent();

	public IFunction2<? super CurrentArgument, ? super EventArgument, ? extends NextArgument> getFunction();

	public IPredicate2<? super CurrentArgument, ? super EventArgument> getGuard();

	public IType1<NextState, NextArgument> getNext();
}
