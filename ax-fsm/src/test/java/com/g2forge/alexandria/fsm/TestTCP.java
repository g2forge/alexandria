package com.g2forge.alexandria.fsm;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.fsm.FSMBuilder;
import com.g2forge.alexandria.fsm.IFSM;
import com.g2forge.alexandria.fsm.IFSMEnum;
import com.g2forge.alexandria.fsm.generic.IGeneric1;
import com.g2forge.alexandria.fsm.transition.ITransition;
import com.g2forge.alexandria.fsm.value.IValue1;

import lombok.Data;

public class TestTCP {
	public interface IClose extends IEvent<Void> {}

	public interface IEvent<V> extends IGeneric1<V> {}

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

	protected static FSMBuilder<IEvent<?>, State> builder;

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
		final IFSM<IEvent<?>, State> fsm = builder.build(State.Established);
		fsm.fire(IValue1.of(IClose.class));
		Assert.assertEquals(State.FinWait1, fsm.getState().getType());
		fsm.fire(IValue1.of(IReceive.class, new Packet(false, true, false)));
		Assert.assertEquals(State.FinWait2, fsm.getState().getType());
		fsm.fire(IValue1.of(IReceive.class, new Packet(false, false, true)));
		Assert.assertEquals(State.TimeWait, fsm.getState().getType());
		fsm.fire(IValue1.of(ITimeout.class));
		Assert.assertEquals(State.Closed, fsm.getState().getType());
	}

	@Test
	public void closePassive() {
		final IFSM<IEvent<?>, State> fsm = builder.build(State.Established);
		fsm.fire(IValue1.of(IReceive.class, new Packet(false, false, true)));
		Assert.assertEquals(State.CloseWait, fsm.getState().getType());
		fsm.fire(IValue1.of(IClose.class));
		Assert.assertEquals(State.LastAck, fsm.getState().getType());
		fsm.fire(IValue1.of(IReceive.class, new Packet(false, true, false)));
		Assert.assertEquals(State.Closed, fsm.getState().getType());
	}

	@Test
	public void closeSimultaneous() {
		final IFSM<IEvent<?>, State> fsm = builder.build(State.Established);
		fsm.fire(IValue1.of(IClose.class));
		Assert.assertEquals(State.FinWait1, fsm.getState().getType());
		fsm.fire(IValue1.of(IReceive.class, new Packet(false, false, true)));
		Assert.assertEquals(State.Closing, fsm.getState().getType());
		fsm.fire(IValue1.of(IReceive.class, new Packet(false, true, false)));
		Assert.assertEquals(State.TimeWait, fsm.getState().getType());
		fsm.fire(IValue1.of(ITimeout.class));
		Assert.assertEquals(State.Closed, fsm.getState().getType());
	}

	@Test
	public void openActiveSimple() {
		final IFSM<IEvent<?>, State> fsm = builder.build(State.Closed);
		fsm.fire(IValue1.of(new Open("thingy", 10)));
		Assert.assertEquals(State.SynSent, fsm.getState().getType());
		fsm.fire(IValue1.of(IReceive.class, new Packet(true, true, false)));
		Assert.assertEquals(State.Established, fsm.getState().getType());
	}

	@Test
	public void openActiveSplit() {
		final IFSM<IEvent<?>, State> fsm = builder.build(State.Closed);
		fsm.fire(IValue1.of(new Open("thingy", 10)));
		Assert.assertEquals(State.SynSent, fsm.getState().getType());
		fsm.fire(IValue1.of(IReceive.class, new Packet(true, false, false)));
		Assert.assertEquals(State.SynReceived, fsm.getState().getType());
		fsm.fire(IValue1.of(IReceive.class, new Packet(false, true, false)));
		Assert.assertEquals(State.Established, fsm.getState().getType());
	}

	@Test
	public void openPassive() {
		final IFSM<IEvent<?>, State> fsm = builder.build(State.Closed);
		fsm.fire(IValue1.of(new Open(null, 0)));
		Assert.assertEquals(State.Listen, fsm.getState().getType());
		fsm.fire(IValue1.of(IReceive.class, new Packet(true, false, false)));
		Assert.assertEquals(State.SynReceived, fsm.getState().getType());
		fsm.fire(IValue1.of(IReceive.class, new Packet(false, true, false)));
		Assert.assertEquals(State.Established, fsm.getState().getType());
	}
}
