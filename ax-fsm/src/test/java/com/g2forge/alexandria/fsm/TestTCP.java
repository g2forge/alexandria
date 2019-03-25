package com.g2forge.alexandria.fsm;

import org.junit.Test;

import com.g2forge.alexandria.java.type.IGeneric;

import lombok.Data;

public class TestTCP {
	public interface IClose extends IEvent<Void> {}

	public interface IEvent<V> extends IGeneric<V> {}

	public interface IReceive extends IEvent<Packet> {}

	public interface ITimeout extends IEvent<Void> {}

	@Data
	public static class Open implements IEvent<Open> {
		protected final String host;
		protected final int port;
	}

	@Data
	public static class Packet {
		protected final boolean syn;
		protected final boolean ack;
		protected final boolean fin;
	}

	public enum State implements IFSMEnum<State> {
		Closed,
		Listen,
		SynReceived,
		SynSent,
		Established,
		FinWait1,
		FinWait2,
		Closing,
		TimeWait,
		CloseWait,
		LastAck
	}

	protected static final FSMBuilder<IEvent<?>, State, ?> builder;

	static {
		builder = new FSMBuilder<>();
		// Open Passive
		// More passive open? Allow local binding?; Set up TCP
		builder.transition(HFSM.transition(State.Closed).event(Open.class).guard((ca, ea) -> (ea.getHost() == null)).next(State.Listen).build());
		// Check Syn sequence number? Reject malformed packets?; Send SynAck
		builder.transition(HFSM.transition(State.Listen).event(IReceive.class).guard((ca, ea) -> ea.isSyn()).next(State.SynReceived).build());
		// Check Ack sequence number? Reject malformed packets?; No action
		builder.transition(HFSM.transition(State.SynReceived).event(IReceive.class).guard((ca, ea) -> ea.isAck()).next(State.Established).build());

		// Open Active
		// Check for valid host/port; Set up TCB, send Syn
		builder.transition(HFSM.transition(State.Closed).event(Open.class).guard((ca, ea) -> (ea.getHost() != null)).next(State.SynSent).build());
		// Check Syn sequence number? Reject malformed packets?; Send ack
		builder.transition(HFSM.transition(State.SynSent).event(IReceive.class).guard((ca, ea) -> ea.isSyn() && !ea.isAck()).next(State.SynReceived).build());
		// Check SynAck sequence number? Reject malformed packets?; Send Ack
		builder.transition(HFSM.transition(State.SynSent).event(IReceive.class).guard((ca, ea) -> ea.isSyn() && ea.isAck()).next(State.Established).build());

		// Close Passive
		// Check sequence number, reject malformed packets; send ack
		builder.transition(HFSM.transition(State.Established).event(IReceive.class).guard((ca, ea) -> ea.isFin()).next(State.CloseWait).build());
		// No guard; Send fin
		builder.transition(HFSM.transition(State.CloseWait).event(IClose.class).next(State.LastAck).build());
		// Check sequence number, reject malformed packets; No action
		builder.transition(HFSM.transition(State.LastAck).event(IReceive.class).guard((ca, ea) -> ea.isAck()).next(State.Closed).build());

		// Close Active
		// No guard; Send fin
		builder.transition(HFSM.transition(State.Established).event(IClose.class).next(State.FinWait1).build());
		// Check sequence number, reject malformed packets; No action
		builder.transition(HFSM.transition(State.FinWait1).event(IReceive.class).guard((ca, ea) -> ea.isAck()).next(State.FinWait2).build());
		// Check sequence number, reject malformed packets; Send ack, start timer
		builder.transition(HFSM.transition(State.FinWait2).event(IReceive.class).guard((ca, ea) -> ea.isFin()).next(State.TimeWait).build());
		builder.transition(HFSM.transition(State.TimeWait).event(ITimeout.class).next(State.Closed).build());

		// Close Simultaneous
		// Check sequence number, reject malformed packets; Send ack
		builder.transition(HFSM.transition(State.FinWait1).event(IReceive.class).guard((ca, ea) -> ea.isFin()).next(State.Closing).build());
		// Check sequence number, reject malformed packets; start timer
		builder.transition(HFSM.transition(State.Closing).event(IReceive.class).guard((ca, ea) -> ea.isAck()).next(State.TimeWait).build());
	}

	@Test
	public void closeActive() {
		final FSMTester<IEvent<?>, State, ?> fsm = new FSMTester<>(builder, State.Established);
		fsm.fire(HFSM.value(IClose.class)).assertStateType(State.FinWait1);
		fsm.fire(HFSM.value(IReceive.class, new Packet(false, true, false))).assertStateType(State.FinWait2);
		fsm.fire(HFSM.value(IReceive.class, new Packet(false, false, true))).assertStateType(State.TimeWait);
		fsm.fire(HFSM.value(ITimeout.class)).assertStateType(State.Closed);
	}

	@Test
	public void closePassive() {
		final FSMTester<IEvent<?>, State, ?> fsm = new FSMTester<>(builder, State.Established);
		fsm.fire(HFSM.value(IReceive.class, new Packet(false, false, true))).assertStateType(State.CloseWait);
		fsm.fire(HFSM.value(IClose.class)).assertStateType(State.LastAck);
		fsm.fire(HFSM.value(IReceive.class, new Packet(false, true, false))).assertStateType(State.Closed);
	}

	@Test
	public void closeSimultaneous() {
		final FSMTester<IEvent<?>, State, ?> fsm = new FSMTester<>(builder, State.Established);
		fsm.fire(HFSM.value(IClose.class)).assertStateType(State.FinWait1);
		fsm.fire(HFSM.value(IReceive.class, new Packet(false, false, true))).assertStateType(State.Closing);
		fsm.fire(HFSM.value(IReceive.class, new Packet(false, true, false))).assertStateType(State.TimeWait);
		fsm.fire(HFSM.value(ITimeout.class)).assertStateType(State.Closed);
	}

	@Test
	public void openActiveSimple() {
		final FSMTester<IEvent<?>, State, ?> fsm = new FSMTester<>(builder, State.Closed);
		fsm.fire(HFSM.value(new Open("thingy", 10))).assertStateType(State.SynSent);
		fsm.fire(HFSM.value(IReceive.class, new Packet(true, true, false))).assertStateType(State.Established);
	}

	@Test
	public void openActiveSplit() {
		final FSMTester<IEvent<?>, State, ?> fsm = new FSMTester<>(builder, State.Closed);
		fsm.fire(HFSM.value(new Open("thingy", 10))).assertStateType(State.SynSent);
		fsm.fire(HFSM.value(IReceive.class, new Packet(true, false, false))).assertStateType(State.SynReceived);
		fsm.fire(HFSM.value(IReceive.class, new Packet(false, true, false))).assertStateType(State.Established);
	}

	@Test
	public void openPassive() {
		final FSMTester<IEvent<?>, State, ?> fsm = new FSMTester<>(builder, State.Closed);
		fsm.fire(HFSM.value(new Open(null, 0))).assertStateType(State.Listen);
		fsm.fire(HFSM.value(IReceive.class, new Packet(true, false, false))).assertStateType(State.SynReceived);
		fsm.fire(HFSM.value(IReceive.class, new Packet(false, true, false))).assertStateType(State.Established);
	}
}
