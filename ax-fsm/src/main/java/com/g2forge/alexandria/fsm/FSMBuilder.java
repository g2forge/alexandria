package com.g2forge.alexandria.fsm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.g2forge.alexandria.fsm.transition.IFSMTransition;
import com.g2forge.alexandria.fsm.value.FSMValue;
import com.g2forge.alexandria.fsm.value.IFSMType;
import com.g2forge.alexandria.fsm.value.IFSMValue;
import com.g2forge.alexandria.java.function.IFunction2;
import com.g2forge.alexandria.java.function.IPredicate2;
import com.g2forge.alexandria.java.type.IGeneric;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class FSMBuilder<Event extends IGeneric<?>, State extends IGeneric<?>, Emission> {
	@Getter(AccessLevel.PROTECTED)
	protected static class FSM<Event extends IGeneric<?>, State extends IGeneric<?>, Emission> implements IFSM<Event, State, Emission> {
		protected final Map<Key, List<IFSMTransition<? extends State, ?, ? extends Event, ?, ? extends State, ?, ? extends Emission>>> map;

		@Getter(AccessLevel.PUBLIC)
		protected IFSMValue<? extends State, ?> state;

		public FSM(Map<Key, List<IFSMTransition<? extends State, ?, ? extends Event, ?, ? extends State, ?, ? extends Emission>>> map, IFSMValue<? extends State, ?> initial) {
			this.map = new LinkedHashMap<>(map.size());
			for (Map.Entry<Key, List<IFSMTransition<? extends State, ?, ? extends Event, ?, ? extends State, ?, ? extends Emission>>> entry : map.entrySet()) {
				this.map.put(entry.getKey(), new ArrayList<>(entry.getValue()));
			}
			this.state = initial;
		}

		@Override
		public Emission fire(IFSMValue<? extends Event, ?> event) {
			final IFSMValue<? extends State, ?> state = getState();

			final List<IFSMTransition<? extends State, ?, ? extends Event, ?, ? extends State, ?, ? extends Emission>> transitions = getMap().get(new Key(state.getType(), event.getType()));
			if ((transitions != null) && !transitions.isEmpty()) for (IFSMTransition<? extends State, ?, ? extends Event, ?, ? extends State, ?, ? extends Emission> transition : transitions) {
				final IPredicate2<?, ?> guard = transition.getGuard();
				if (guard != null) {
					@SuppressWarnings({ "unchecked", "rawtypes" })
					final boolean accept = ((IPredicate2) guard).test(state.getValue(), event.getValue());
					if (!accept) continue;
				}

				final IFunction2<?, ?, ?> argumentFunction = transition.getArgument();
				@SuppressWarnings({ "unchecked", "rawtypes" })
				final Object argumentValue = (argumentFunction != null) ? ((IFunction2) argumentFunction).apply(state.getValue(), event.getValue()) : null;
				@SuppressWarnings({ "unchecked", "rawtypes" })
				final FSMValue<? extends State, ?> next = new FSMValue(transition.getNext(), argumentValue);
				this.state = optimize(next);

				final IFunction2<?, ?, ?> emitFunction = transition.getEmit();
				@SuppressWarnings({ "unchecked", "rawtypes" })
				final Emission emissionValue = (emitFunction != null) ? (Emission) ((IFunction2) emitFunction).apply(state.getValue(), event.getValue()) : null;
				return emissionValue;
			}
			throw new FSMDisallowedEventException(state.getType(), event.getType());
		}

		protected IFSMValue<? extends State, ?> optimize(final IFSMValue<? extends State, ?> value) {
			if (value.getType() instanceof IFSMValue) {
				@SuppressWarnings("unchecked")
				final IFSMValue<? extends State, ?> cast = (IFSMValue<? extends State, ?>) value.getType();
				if ((value.getType() == cast.getType()) && (value.getValue() == cast.getValue())) { return cast; }
			}
			return value;
		}
	}

	@Data
	@RequiredArgsConstructor
	protected static class Key {
		protected final IFSMType<?, ?> current;

		protected final IFSMType<?, ?> event;

		public Key(IFSMTransition<?, ?, ?, ?, ?, ?, ?> transition) {
			this.current = transition.getCurrent();
			this.event = transition.getEvent();
		}
	}

	protected Map<Key, List<IFSMTransition<? extends State, ?, ? extends Event, ?, ? extends State, ?, ? extends Emission>>> map = new LinkedHashMap<>();

	protected boolean used = true;

	/**
	 * Create an instance of this finite state machine, starting in the specified initial state. Note that further changes to this builder after a call to this
	 * method will not affect the FSM, though they will affect FSMs constucted in the future.
	 * 
	 * @param initial The initial state for the resulting FSM.
	 * @return A finite state machine.
	 */
	public IFSM<Event, State, Emission> build(IFSMValue<? extends State, ?> initial) {
		used = true;
		return new FSM<Event, State, Emission>(map, initial);
	}

	/**
	 * Add a transition to this builder.
	 * 
	 * @param transition The transition to add to this builder.
	 * @return <code>this</code>
	 */
	public FSMBuilder<Event, State, Emission> transition(IFSMTransition<? extends State, ?, ? extends Event, ?, ? extends State, ?, ? extends Emission> transition) {
		final Key key = new Key(transition);
		if (used) {
			// If we already constructed FSMs with the map, then create a new one.
			map = new LinkedHashMap<>(map);
			used = false;
		}
		final List<IFSMTransition<? extends State, ?, ? extends Event, ?, ? extends State, ?, ? extends Emission>> transitions = map.computeIfAbsent(key, ignore -> new ArrayList<>());

		// Check for duplicates
		final Optional<IFSMTransition<? extends State, ?, ? extends Event, ?, ? extends State, ?, ? extends Emission>> duplicate = transitions.stream().filter(t -> (t.getGuard() == null) || (transition.getGuard() == null) || t.getGuard().equals(transition.getGuard())).findAny();
		if (duplicate.isPresent()) throw new IllegalArgumentException(String.format("Duplicate transitions with key %s: %s and %s!", key, transition, duplicate.get()));

		// Add the new transition
		transitions.add(transition);
		return this;
	}
}
