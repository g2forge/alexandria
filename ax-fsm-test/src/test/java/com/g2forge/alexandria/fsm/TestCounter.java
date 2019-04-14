package com.g2forge.alexandria.fsm;

import static com.g2forge.alexandria.fsm.HFSM.transition;
import static com.g2forge.alexandria.fsm.HFSM.value;

import org.junit.Test;

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

	protected static final FSMBuilder<Event, State, Object, Object> builder;

	static {
		builder = new FSMBuilder<>();
		builder.transition(transition(State.Zero, Event.Up, State.One));
		builder.transition(transition(State.One, Event.Up, State.Two));
		builder.transition(transition(State.Two, Event.Down, State.One));
		builder.transition(transition(State.One, Event.Down, State.Zero));
	}

	@Test
	public void base() {
		final FSMTester<Event, State, Object, Object> tester = new FSMTester<>(builder, State.Zero);
		tester.fire(Event.Up).assertState(State.One);
		tester.fire(value(Event.Up)).assertState(State.Two);
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
