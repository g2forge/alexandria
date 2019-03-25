package com.g2forge.alexandria.fsm.transition;

import com.g2forge.alexandria.fsm.value.IFSMType;
import com.g2forge.alexandria.java.function.IFunction2;
import com.g2forge.alexandria.java.function.IPredicate2;
import com.g2forge.alexandria.java.type.IGeneric;

public interface IFSMTransition<CurrentState extends IGeneric<CurrentArgument>, CurrentArgument, Event extends IGeneric<EventArgument>, EventArgument, NextState extends IGeneric<NextArgument>, NextArgument, Output> {
	public IFunction2<? super CurrentArgument, ? super EventArgument, ? extends NextArgument> getArgument();

	public IFSMType<CurrentState, CurrentArgument> getCurrent();

	public IFSMType<Event, EventArgument> getEvent();

	public IPredicate2<? super CurrentArgument, ? super EventArgument> getGuard();

	public IFSMType<NextState, NextArgument> getNext();
	
	public IFunction2<? super CurrentArgument, ? super EventArgument, ? extends Output> getOutput();
}
