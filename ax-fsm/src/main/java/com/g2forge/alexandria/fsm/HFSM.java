package com.g2forge.alexandria.fsm;

import com.g2forge.alexandria.fsm.transition.FSMTransition;
import com.g2forge.alexandria.fsm.transition.IFSMTransition;
import com.g2forge.alexandria.fsm.transition.builder.CurrentFSMTransitionBuilder;
import com.g2forge.alexandria.fsm.value.ClassFSMType;
import com.g2forge.alexandria.fsm.value.FSMValue;
import com.g2forge.alexandria.fsm.value.IFSMType;
import com.g2forge.alexandria.fsm.value.IFSMValue;
import com.g2forge.alexandria.java.marker.Helpers;
import com.g2forge.alexandria.java.type.IGeneric;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HFSM {
	public static <CurrentState extends IGeneric<CurrentArgument>, CurrentArgument> CurrentFSMTransitionBuilder<CurrentState, CurrentArgument> transition(IFSMType<CurrentState, CurrentArgument> current) {
		return new CurrentFSMTransitionBuilder<>(current);
	}

	public static <CurrentState extends IGeneric<CurrentArgument>, CurrentArgument, Event extends IGeneric<EventArgument>, EventArgument, NextState extends IGeneric<Void>, Emission> IFSMTransition<CurrentState, CurrentArgument, Event, EventArgument, NextState, Void, Emission> transition(IFSMType<CurrentState, CurrentArgument> current, IFSMType<Event, EventArgument> event, IFSMType<NextState, Void> next) {
		return new FSMTransition<>(current, event, null, next, (ca, ea) -> null, null);
	}
	
	public static <CurrentState extends IGeneric<CurrentArgument>, CurrentArgument, Event extends IGeneric<EventArgument>, EventArgument, NextState extends IGeneric<Void>, Emission> IFSMTransition<CurrentState, CurrentArgument, Event, EventArgument, NextState, Void, Emission> transition(IFSMType<CurrentState, CurrentArgument> current, IFSMType<Event, EventArgument> event, IFSMType<NextState, Void> next, Emission emission) {
		return new FSMTransition<>(current, event, null, next, (ca, ea) -> null, (ca, ea) -> emission);
	}

	public static <G extends IGeneric<T>, T> IFSMType<G, T> type(Class<G> type) {
		return new ClassFSMType<>(type);
	}

	public static <G extends IGeneric<T>, T> IFSMType<G, T> type(T value) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final Class<G> type = (Class) value.getClass();
		return type(type);
	}

	public static <G extends IGeneric<Void>> IFSMValue<G, Void> value(Class<G> type) {
		return new FSMValue<>(type(type), null);
	}

	public static <G extends IGeneric<T>, T> IFSMValue<G, T> value(Class<G> type, T value) {
		return new FSMValue<>(type(type), value);
	}

	public static <G extends IGeneric<G>> IFSMValue<G, G> value(G value) {
		return new FSMValue<>(type(value), value);
	}

	public static <G extends IGeneric<T>, T> IFSMValue<G, T> value(IFSMType<G, T> type, T value) {
		return new FSMValue<>(type, value);
	}

	public static <G extends IGeneric<Void>> IFSMValue<G, Void> value(IFSMType<G, Void> type) {
		return new FSMValue<>(type, null);
	}
}
