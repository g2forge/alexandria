package com.g2forge.alexandria.fsm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.g2forge.alexandria.fsm.generic.IGeneric1;
import com.g2forge.alexandria.fsm.transition.ITransition;
import com.g2forge.alexandria.fsm.type.IType1;
import com.g2forge.alexandria.fsm.value.IValue1;
import com.g2forge.alexandria.fsm.value.Value1;
import com.g2forge.alexandria.java.function.IFunction2;
import com.g2forge.alexandria.java.function.IPredicate2;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class FSMBuilder<E extends IGeneric1<?>, S extends IGeneric1<?>> {
	@Getter(AccessLevel.PROTECTED)
	protected static class FSM<E extends IGeneric1<?>, S extends IGeneric1<?>> implements IFSM<E, S> {
		protected final Map<Key, List<ITransition<? extends S, ?, ? extends E, ?, ? extends S, ?>>> map;

		@Getter(AccessLevel.PUBLIC)
		protected IValue1<? extends S, ?> state;

		public FSM(Map<Key, List<ITransition<? extends S, ?, ? extends E, ?, ? extends S, ?>>> map, IValue1<? extends S, ?> initial) {
			this.map = new LinkedHashMap<>(map.size());
			for (Map.Entry<Key, List<ITransition<? extends S, ?, ? extends E, ?, ? extends S, ?>>> entry : map.entrySet()) {
				this.map.put(entry.getKey(), new ArrayList<>(entry.getValue()));
			}
			this.state = initial;
		}

		@Override
		public void fire(IValue1<? extends E, ?> event) {
			final IValue1<? extends S, ?> state = getState();

			final List<ITransition<? extends S, ?, ? extends E, ?, ? extends S, ?>> transitions = getMap().get(new Key(state.getType(), event.getType()));
			if ((transitions == null) || transitions.isEmpty()) throw new FSMDisallowedEventException(state.getType(), event.getType());
			for (ITransition<? extends S, ?, ? extends E, ?, ? extends S, ?> transition : transitions) {
				final IPredicate2<?, ?> guard = transition.getGuard();
				if (guard != null) {
					@SuppressWarnings({ "unchecked", "rawtypes" })
					final boolean accept = ((IPredicate2) guard).test(state.getValue(), event.getValue());
					if (!accept) continue;
				}

				final IFunction2<?, ?, ?> function = transition.getFunction();
				@SuppressWarnings({ "unchecked", "rawtypes" })
				final Value1<? extends S, ?> next = new Value1(transition.getNext(), (function != null) ? ((IFunction2) function).apply(state.getValue(), event.getValue()) : null);
				this.state = optimize(next);
				return;
			}
			throw new FSMDisallowedEventException(state.getType(), event.getType());
		}

		protected IValue1<? extends S, ?> optimize(final IValue1<? extends S, ?> value) {
			if (value.getType() instanceof IValue1) {
				@SuppressWarnings("unchecked")
				final IValue1<? extends S, ?> cast = (IValue1<? extends S, ?>) value.getType();
				if ((value.getType() == cast.getType()) && (value.getValue() == cast.getValue())) { return cast; }
			}
			return value;
		}
	}

	@Data
	@RequiredArgsConstructor
	protected static class Key {
		protected final IType1<?, ?> current;

		protected final IType1<?, ?> event;

		public Key(ITransition<?, ?, ?, ?, ?, ?> transition) {
			this.current = transition.getCurrent();
			this.event = transition.getEvent();
		}
	}

	protected final Map<Key, List<ITransition<? extends S, ?, ? extends E, ?, ? extends S, ?>>> map = new LinkedHashMap<>();

	public IFSM<E, S> build(IValue1<? extends S, ?> initial) {
		return new FSM<E, S>(map, initial);
	}

	public FSMBuilder<E, S> transition(ITransition<? extends S, ?, ? extends E, ?, ? extends S, ?> transition) {
		final Key key = new Key(transition);
		List<ITransition<? extends S, ?, ? extends E, ?, ? extends S, ?>> transitions = map.get(key);
		if (transitions == null) map.put(key, transitions = new ArrayList<>());

		// Check for duplicates
		final Optional<ITransition<? extends S, ?, ? extends E, ?, ? extends S, ?>> duplicate = transitions.stream().filter(t -> (t.getGuard() == null) || (transition.getGuard() == null) || t.getGuard().equals(transition.getGuard())).findAny();
		if (duplicate.isPresent()) throw new IllegalArgumentException(String.format("Duplicate transitions with key %s: %s and %s!", key, transition, duplicate.get()));

		transitions.add(transition);
		return this;
	}
}
