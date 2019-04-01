package com.g2forge.alexandria.fsm;

import org.junit.Test;
import static com.g2forge.alexandria.fsm.HFSM.*;

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

	protected static final FSMBuilder<Event, State, Integer> builder;

	static {
		builder = new FSMBuilder<Event, State, Integer>();
		builder.transition(transition(State.Zero, Event.Up, State.One, 0));
		builder.transition(transition(State.One, Event.Up, State.Two, 1));
		builder.transition(transition(State.Two, Event.Down, State.One));
		builder.transition(transition(State.One, Event.Down, State.Zero));
	}

	@Test
	public void base() {
		final FSMTester<Event, State, Integer> tester = new FSMTester<>(builder, State.Zero);
		tester.assertEmission(0, Event.Up).assertState(State.One);
		tester.assertEmission(1, value(Event.Up)).assertState(State.Two);
		tester.assertEmission(null, Event.Down).assertStateType(State.One);
		tester.assertEmission(null, Event.Down).assertStateType(State.Zero);
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
