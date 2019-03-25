package com.g2forge.alexandria.fsm.transition.builder;

import com.g2forge.alexandria.fsm.generic.IGeneric1;
import com.g2forge.alexandria.fsm.type.ClassType1;
import com.g2forge.alexandria.fsm.type.IType1;

import lombok.Data;

@Data
public class CurrentTransitionBuilder<CS extends IGeneric1<CA>, CA> {
	protected final IType1<CS, CA> current;

	public <E extends IGeneric1<EA>, EA> EventTransitionBuilder<CS, CA, E, EA> event(Class<E> event) {
		return event(new ClassType1<E, EA>(event));
	}

	public <E extends IGeneric1<EA>, EA> EventTransitionBuilder<CS, CA, E, EA> event(IType1<E, EA> event) {
		return new EventTransitionBuilder<>(getCurrent(), event);
	}
}