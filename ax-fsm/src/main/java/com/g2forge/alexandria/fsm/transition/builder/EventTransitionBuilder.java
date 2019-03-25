package com.g2forge.alexandria.fsm.transition.builder;

import com.g2forge.alexandria.fsm.generic.IGeneric1;
import com.g2forge.alexandria.fsm.type.ClassType1;
import com.g2forge.alexandria.fsm.type.IType1;
import com.g2forge.alexandria.java.function.IPredicate2;

import lombok.Data;

@Data
public class EventTransitionBuilder<CS extends IGeneric1<CA>, CA, E extends IGeneric1<EA>, EA> {
	protected final IType1<CS, CA> current;

	protected final IType1<E, EA> event;

	protected IPredicate2<? super CA, ? super EA> guard;

	public EventTransitionBuilder<CS, CA, E, EA> guard(IPredicate2<? super CA, ? super EA> guard) {
		this.guard = guard;
		return this;
	}

	public <NS extends IGeneric1<NA>, NA> NextTransitionBuilder<CS, CA, E, EA, NS, NA> next(Class<NS> next) {
		return next(new ClassType1<>(next));
	}

	public <NS extends IGeneric1<NA>, NA> NextTransitionBuilder<CS, CA, E, EA, NS, NA> next(IType1<NS, NA> next) {
		return new NextTransitionBuilder<>(getCurrent(), getEvent(), getGuard(), next);
	}
}