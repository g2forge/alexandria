package com.g2forge.alexandria.fsm;

import org.junit.Test;

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
	
	protected static final FSMBuilder<Event, State> builder;
	
	static {
		builder = new FSMBuilder<Event, State>();
		builder.transition(ITransition.of(State.Zero, Event.Up, State.One));
		builder.transition(ITransition.of(State.One, Event.Up, State.Two));
		builder.transition(ITransition.of(State.Two, Event.Down, State.One));
		builder.transition(ITransition.of(State.One, Event.Down, State.Zero));
	}

	@Test
	public void base() {
		final FSMTester<Event, State> tester = new FSMTester<>(builder, State.Zero);
		tester.fire(Event.Up).assertState(State.One);
		tester.fire(IValue1.of(Event.Up)).assertState(State.Two);
		tester.fire(Event.Down).assertStateType(State.One);
		tester.fire(Event.Down).assertStateType(State.Zero);
	}

	@Test(expected = FSMDisallowedEventException.class)
	public void disallowTwoUp() {
		new FSMTester<>(builder, State.Two).fire(Event.Up);
	}

	@Test(expected = FSMDisallowedEventException.class)
	public void disallowZeroDown() {
		new FSMTester<>(builder, State.Zero).fire(Event.Down);
	}
}
