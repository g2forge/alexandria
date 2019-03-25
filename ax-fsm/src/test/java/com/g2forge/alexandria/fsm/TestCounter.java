package com.g2forge.alexandria.fsm;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.fsm.FSMBuilder;
import com.g2forge.alexandria.fsm.IFSM;
import com.g2forge.alexandria.fsm.IFSMEnum;
import com.g2forge.alexandria.fsm.transition.ITransition;
import com.g2forge.alexandria.fsm.value.IValue1;

public class TestCounter {
	public static enum Event implements IFSMEnum<Event> {
		Up,
		Down;
	}

	public static enum State implements IFSMEnum<State> {
		Zero,
		One,
		Two;
	}

	@Test
	public void test() {
		final FSMBuilder<Event, State> builder = new FSMBuilder<Event, State>();
		builder.transition(ITransition.of(State.Zero, Event.Up, State.One));
		builder.transition(ITransition.of(State.One, Event.Up, State.Two));
		builder.transition(ITransition.of(State.Two, Event.Down, State.One));
		builder.transition(ITransition.of(State.One, Event.Down, State.Zero));

		final IFSM<Event, State> fsm = builder.build(State.Zero);
		Assert.assertEquals(State.Zero, fsm.getState());
		fsm.fire(Event.Up);
		Assert.assertEquals(State.One, fsm.getState());
		fsm.fire(IValue1.of(Event.Up));
		Assert.assertEquals(State.Two, fsm.getState());
		fsm.fire(Event.Down);
		Assert.assertEquals(State.One, fsm.getState().getType());
		fsm.fire(Event.Down);
		Assert.assertEquals(State.Zero, fsm.getState().getType());
	}
}
