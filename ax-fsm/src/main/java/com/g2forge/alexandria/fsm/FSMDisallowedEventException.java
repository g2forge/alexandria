package com.g2forge.alexandria.fsm;

import com.g2forge.alexandria.fsm.type.IType1;

import lombok.Getter;

@Getter
public class FSMDisallowedEventException extends RuntimeException {
	private static final long serialVersionUID = -5808247838159061358L;

	protected final IType1<?, ?> state;

	protected final IType1<?, ?> event;

	public FSMDisallowedEventException(IType1<?, ?> state, IType1<?, ?> event) {
		super(String.format("There is no transition from state %1$s to handle event %2$s", state, event));
		this.state = state;
		this.event = event;
	}
}
