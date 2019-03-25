package com.g2forge.alexandria.fsm.transition.builder;

import com.g2forge.alexandria.fsm.generic.IGeneric1;
import com.g2forge.alexandria.fsm.generic.type.IType1;
import com.g2forge.alexandria.fsm.transition.ITransition;
import com.g2forge.alexandria.fsm.transition.Transition;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction2;
import com.g2forge.alexandria.java.function.IPredicate2;

import lombok.Data;

@Data
public class NextTransitionBuilder<CS extends IGeneric1<CA>, CA, E extends IGeneric1<EA>, EA, NS extends IGeneric1<NA>, NA> {
	protected final IType1<CS, CA> current;

	protected final IType1<E, EA> event;

	protected final IPredicate2<? super CA, ? super EA> guard;

	protected final IType1<NS, NA> next;

	public ITransition<CS, CA, E, EA, NS, NA> function() {
		return function((IFunction2<? super CA, ? super EA, ? extends NA>) null);
	}

	public ITransition<CS, CA, E, EA, NS, NA> function(final IConsumer2<? super CA, ? super EA> consumer) {
		return function((ca, ea) -> {
			consumer.accept(ca, ea);
			return null;
		});
	}

	public ITransition<CS, CA, E, EA, NS, NA> function(IFunction2<? super CA, ? super EA, ? extends NA> function) {
		return new Transition<>(getCurrent(), getEvent(), getGuard(), getNext(), function);
	}
}