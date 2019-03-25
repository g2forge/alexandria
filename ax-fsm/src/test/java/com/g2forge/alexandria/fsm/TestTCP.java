package com.g2forge.alexandria.fsm;

import org.junit.Test;

import com.g2forge.alexandria.fsm.generic.value.IValue1;
import com.g2forge.alexandria.fsm.transition.ITransition;
import com.g2forge.alexandria.java.typed.IGeneric;

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

	protected static final FSMBuilder<IEvent<?>, State> builder;

	static {
		builder = new FSMBuilder<>();
		// Open Passive
		// More passive open? Allow local binding?; Set up TCP
		builder.transition(ITransition.of(State.Closed).event(Open.class).guard((ca, ea) -> (ea.getHost() == null)).next(State.Listen).function());
		// Check Syn sequence number? Reject malformed packets?; Send SynAck
		builder.transition(ITransition.of(State.Listen).event(IReceive.class).guard((ca, ea) -> ea.isSyn()).next(State.SynReceived).function());
		// Check Ack sequence number? Reject malformed packets?; No action
		builder.transition(ITransition.of(State.SynReceived).event(IReceive.class).guard((ca, ea) -> ea.isAck()).next(State.Established).function());

		// Open Active
		// Check for valid host/port; Set up TCB, send Syn
		builder.transition(ITransition.of(State.Closed).event(Open.class).guard((ca, ea) -> (ea.getHost() != null)).next(State.SynSent).function());
		// Check Syn sequence number? Reject malformed packets?; Send ack
		builder.transition(ITransition.of(State.SynSent).event(IReceive.class).guard((ca, ea) -> ea.isSyn() && !ea.isAck()).next(State.SynReceived).function());
		// Check SynAck sequence number? Reject malformed packets?; Send Ack
		builder.transition(ITransition.of(State.SynSent).event(IReceive.class).guard((ca, ea) -> ea.isSyn() && ea.isAck()).next(State.Established).function());

		// Close Passive
		// Check sequence number, reject malformed packets; send ack
		builder.transition(ITransition.of(State.Established).event(IReceive.class).guard((ca, ea) -> ea.isFin()).next(State.CloseWait).function());
		// No guard; Send fin
		builder.transition(ITransition.of(State.CloseWait).event(IClose.class).next(State.LastAck).function());
		// Check sequence number, reject malformed packets; No action
		builder.transition(ITransition.of(State.LastAck).event(IReceive.class).guard((ca, ea) -> ea.isAck()).next(State.Closed).function());

		// Close Active
		// No guard; Send fin
		builder.transition(ITransition.of(State.Established).event(IClose.class).next(State.FinWait1).function());
		// Check sequence number, reject malformed packets; No action
		builder.transition(ITransition.of(State.FinWait1).event(IReceive.class).guard((ca, ea) -> ea.isAck()).next(State.FinWait2).function());
		// Check sequence number, reject malformed packets; Send ack, start timer
		builder.transition(ITransition.of(State.FinWait2).event(IReceive.class).guard((ca, ea) -> ea.isFin()).next(State.TimeWait).function());
		builder.transition(ITransition.of(State.TimeWait).event(ITimeout.class).next(State.Closed).function());

		// Close Simultaneous
		// Check sequence number, reject malformed packets; Send ack
		builder.transition(ITransition.of(State.FinWait1).event(IReceive.class).guard((ca, ea) -> ea.isFin()).next(State.Closing).function());
		// Check sequence number, reject malformed packets; start timer
		builder.transition(ITransition.of(State.Closing).event(IReceive.class).guard((ca, ea) -> ea.isAck()).next(State.TimeWait).function());
	}

	@Test
	public void closeActive() {
		final FSMTester<IEvent<?>, State> fsm = new FSMTester<>(builder, State.Established);
		fsm.fire(IValue1.of(IClose.class)).assertStateType(State.FinWait1);
		fsm.fire(IValue1.of(IReceive.class, new Packet(false, true, false))).assertStateType(State.FinWait2);
		fsm.fire(IValue1.of(IReceive.class, new Packet(false, false, true))).assertStateType(State.TimeWait);
		fsm.fire(IValue1.of(ITimeout.class)).assertStateType(State.Closed);
	}

	@Test
	public void closePassive() {
		final FSMTester<IEvent<?>, State> fsm = new FSMTester<>(builder, State.Established);
		fsm.fire(IValue1.of(IReceive.class, new Packet(false, false, true))).assertStateType(State.CloseWait);
		fsm.fire(IValue1.of(IClose.class)).assertStateType(State.LastAck);
		fsm.fire(IValue1.of(IReceive.class, new Packet(false, true, false))).assertStateType(State.Closed);
	}

	@Test
	public void closeSimultaneous() {
		final FSMTester<IEvent<?>, State> fsm = new FSMTester<>(builder, State.Established);
		fsm.fire(IValue1.of(IClose.class)).assertStateType(State.FinWait1);
		fsm.fire(IValue1.of(IReceive.class, new Packet(false, false, true))).assertStateType(State.Closing);
		fsm.fire(IValue1.of(IReceive.class, new Packet(false, true, false))).assertStateType(State.TimeWait);
		fsm.fire(IValue1.of(ITimeout.class)).assertStateType(State.Closed);
	}

	@Test
	public void openActiveSimple() {
		final FSMTester<IEvent<?>, State> fsm = new FSMTester<>(builder, State.Closed);
		fsm.fire(IValue1.of(new Open("thingy", 10))).assertStateType(State.SynSent);
		fsm.fire(IValue1.of(IReceive.class, new Packet(true, true, false))).assertStateType(State.Established);
	}

	@Test
	public void openActiveSplit() {
		final FSMTester<IEvent<?>, State> fsm = new FSMTester<>(builder, State.Closed);
		fsm.fire(IValue1.of(new Open("thingy", 10))).assertStateType(State.SynSent);
		fsm.fire(IValue1.of(IReceive.class, new Packet(true, false, false))).assertStateType(State.SynReceived);
		fsm.fire(IValue1.of(IReceive.class, new Packet(false, true, false))).assertStateType(State.Established);
	}

	@Test
	public void openPassive() {
		final FSMTester<IEvent<?>, State> fsm = new FSMTester<>(builder, State.Closed);
		fsm.fire(IValue1.of(new Open(null, 0))).assertStateType(State.Listen);
		fsm.fire(IValue1.of(IReceive.class, new Packet(true, false, false))).assertStateType(State.SynReceived);
		fsm.fire(IValue1.of(IReceive.class, new Packet(false, true, false))).assertStateType(State.Established);
	}
}
