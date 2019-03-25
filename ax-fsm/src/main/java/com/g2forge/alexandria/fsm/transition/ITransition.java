package com.g2forge.alexandria.fsm.transition;

import com.g2forge.alexandria.fsm.generic.IGeneric1;
import com.g2forge.alexandria.fsm.generic.type.ClassType1;
import com.g2forge.alexandria.fsm.generic.type.IType1;
import com.g2forge.alexandria.fsm.transition.builder.CurrentTransitionBuilder;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction2;
import com.g2forge.alexandria.java.function.IPredicate2;

public interface ITransition<CS extends IGeneric1<CA>, CA, E extends IGeneric1<EA>, EA, NS extends IGeneric1<NA>, NA> {
	public static <CS extends IGeneric1<CA>, CA> CurrentTransitionBuilder<CS, CA> of(Class<CS> current) {
		return of(new ClassType1<>(current));
	}

	public static <CS extends IGeneric1<CA>, CA> CurrentTransitionBuilder<CS, CA> of(IType1<CS, CA> current) {
		return new CurrentTransitionBuilder<>(current);
	}

	public static <CS extends IGeneric1<CA>, CA, E extends IGeneric1<EA>, EA, NS extends IGeneric1<NA>, NA> ITransition<CS, CA, E, EA, NS, NA> of(IType1<CS, CA> current, IType1<E, EA> event, IType1<NS, NA> next, IFunction2<? super CA, ? super EA, ? extends NA> function) {
		return new Transition<>(current, event, null, next, function);
	}

	public static <CS extends IGeneric1<CA>, CA, E extends IGeneric1<EA>, EA, NS extends IGeneric1<Void>> ITransition<CS, CA, E, EA, NS, Void> of(IType1<CS, CA> current, IType1<E, EA> event, IType1<NS, Void> next) {
		return ITransition.of(current, event, next, (ca, ea) -> {
			return null;
		});
	}

	public static <CS extends IGeneric1<CA>, CA, E extends IGeneric1<EA>, EA, NS extends IGeneric1<Void>> ITransition<CS, CA, E, EA, NS, Void> of(IType1<CS, CA> current, IType1<E, EA> event, IType1<NS, Void> next, IConsumer2<? super CA, ? super EA> function) {
		return ITransition.of(current, event, next, (ca, ea) -> {
			function.accept(ca, ea);
			return null;
		});
	}

	public IType1<CS, CA> getCurrent();

	public IType1<E, EA> getEvent();

	public IFunction2<? super CA, ? super EA, ? extends NA> getFunction();

	public IPredicate2<? super CA, ? super EA> getGuard();

	public IType1<NS, NA> getNext();
}
