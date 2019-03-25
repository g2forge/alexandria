package com.g2forge.alexandria.fsm;

import com.g2forge.alexandria.fsm.value.IFSMType;

import lombok.Getter;

/**
 * Indicates that the {@link #getEvent() event} is not allowed in the {@link #getState() state}.
 */
@Getter
public class FSMDisallowedEventException extends RuntimeException {
	private static final long serialVersionUID = -5808247838159061358L;

	protected final IFSMType<?, ?> state;

	protected final IFSMType<?, ?> event;

	public FSMDisallowedEventException(IFSMType<?, ?> state, IFSMType<?, ?> event) {
		super(String.format("There is no transition from state %1$s to handle event %2$s", state, event));
		this.state = state;
		this.event = event;
	}
}
