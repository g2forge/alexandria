package com.g2forge.alexandria.fsm;

import com.g2forge.alexandria.fsm.value.IFSMType;
import com.g2forge.alexandria.fsm.value.IFSMValue;
import com.g2forge.alexandria.java.type.IGeneric;
import com.g2forge.alexandria.test.HAssert;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FSMTester<Event extends IGeneric<?>, State extends IGeneric<?>, Output> {
	protected final IFSM<Event, State, Output> fsm;

	public FSMTester(FSMBuilder<Event, State, Output> builder, IFSMValue<? extends State, ?> initial) {
		this(builder.build(initial));
		assertState(initial);
	}

	public FSMTester<Event, State, Output> assertOutput(Output expected, IFSMValue<? extends Event, ?> event) {
		final Output actual = getFsm().fire(event);
		HAssert.assertEquals(expected, actual);
		return this;
	}

	public FSMTester<Event, State, Output> assertState(IFSMValue<? extends State, ?> expected) {
		HAssert.assertEquals(expected, getFsm().getState());
		return this;
	}

	public FSMTester<Event, State, Output> assertStateType(IFSMType<? extends State, ?> expected) {
		HAssert.assertEquals(expected, getFsm().getState().getType());
		return this;
	}

	public FSMTester<Event, State, Output> fire(IFSMValue<? extends Event, ?> event) {
		getFsm().fire(event);
		return this;
	}
}
