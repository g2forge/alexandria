package com.g2forge.alexandria.fsm;

import com.g2forge.alexandria.fsm.value.IFSMType;
import com.g2forge.alexandria.fsm.value.IFSMValue;
import com.g2forge.alexandria.java.type.IGeneric;
import com.g2forge.alexandria.test.HAssert;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FSMTester<Event extends IGeneric<?>, State extends IGeneric<?>, Emission, Output> {
	protected final IFSM<Event, State, Emission, Output> fsm;

	public FSMTester(FSMBuilder<Event, State, Emission, Output> builder, IFSMValue<? extends State, ?> initial) {
		this(builder.build(initial));
		assertState(initial);
	}

	public FSMTester<Event, State, Emission, Output> assertEmission(Emission expected, IFSMValue<? extends Event, ?> event) {
		final Emission actual = getFsm().fire(event);
		HAssert.assertEquals(expected, actual);
		return this;
	}

	public FSMTester<Event, State, Emission, Output> assertOutput(Output expected) {
		HAssert.assertEquals(expected, getFsm().getOutput());
		return this;
	}

	public FSMTester<Event, State, Emission, Output> assertState(IFSMValue<? extends State, ?> expected) {
		HAssert.assertEquals(expected, getFsm().getState());
		return this;
	}

	public FSMTester<Event, State, Emission, Output> assertStateType(IFSMType<? extends State, ?> expected) {
		HAssert.assertEquals(expected, getFsm().getState().getType());
		return this;
	}

	public FSMTester<Event, State, Emission, Output> fire(IFSMValue<? extends Event, ?> event) {
		getFsm().fire(event);
		return this;
	}
}
