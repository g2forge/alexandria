package com.g2forge.alexandria.fsm;

import com.g2forge.alexandria.fsm.generic.IGeneric1;
import com.g2forge.alexandria.fsm.type.IType1;
import com.g2forge.alexandria.fsm.value.IValue1;
import com.g2forge.alexandria.test.HAssert;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FSMTester<E extends IGeneric1<?>, S extends IGeneric1<?>> {
	protected final IFSM<E, S> fsm;

	public FSMTester(FSMBuilder<E, S> builder, IValue1<? extends S, ?> initial) {
		this(builder.build(initial));
		assertState(initial);
	}

	public FSMTester<E, S> assertState(IValue1<? extends S, ?> expected) {
		HAssert.assertEquals(expected, getFsm().getState());
		return this;
	}

	public FSMTester<E, S> assertStateType(IType1<? extends S, ?> expected) {
		HAssert.assertEquals(expected, getFsm().getState().getType());
		return this;
	}

	public FSMTester<E, S> fire(IValue1<? extends E, ?> event) {
		getFsm().fire(event);
		return this;
	}
}
