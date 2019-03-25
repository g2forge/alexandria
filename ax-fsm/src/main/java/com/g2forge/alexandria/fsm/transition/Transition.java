package com.g2forge.alexandria.fsm.transition;

import com.g2forge.alexandria.fsm.generic.IGeneric1;
import com.g2forge.alexandria.fsm.type.IType1;
import com.g2forge.alexandria.java.function.IFunction2;
import com.g2forge.alexandria.java.function.IPredicate2;

import lombok.Data;

@Data
public class Transition<CS extends IGeneric1<CA>, CA, E extends IGeneric1<EA>, EA, NS extends IGeneric1<NA>, NA> implements ITransition<CS, CA, E, EA, NS, NA> {
	protected final IType1<CS, CA> current;

	protected final IType1<E, EA> event;

	protected final IPredicate2<? super CA, ? super EA> guard;

	protected final IType1<NS, NA> next;

	protected final IFunction2<? super CA, ? super EA, ? extends NA> function;
}
