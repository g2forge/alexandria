package com.g2forge.alexandria.fsm;

import static com.g2forge.alexandria.fsm.HFSM.transition;
import static com.g2forge.alexandria.fsm.HFSM.type;
import static com.g2forge.alexandria.fsm.HFSM.value;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.g2forge.alexandria.java.concurrent.HConcurrent;
import com.g2forge.alexandria.java.type.IGeneric;
import com.g2forge.alexandria.test.HAssert;

import lombok.AccessLevel;
import lombok.Getter;

@RunWith(Parameterized.class)
public class TestThreadSafety {
	public static interface Event extends IGeneric<Integer> {}

	public static interface State extends IGeneric<Boolean> {}

	@Parameters
	public static Collection<Object[]> parameters() {
		return Arrays.asList(new Object[][] { { false }, { true } });
	}

	@Parameter
	@Getter(AccessLevel.PROTECTED)
	public boolean safe;

	@Test
	public void base() throws InterruptedException {
		// Run multiple trials with multiple threads
		final int threads = 3, trials = 20;
		for (int t = 0; t < trials; t++) {
			final List<Integer> fired = new Vector<>();
			final FSMTester<Event, State, Object, Object> tester;

			{ // Create the state machine
				 // This is a very bad state machine, it adds things to "fired" outside of the formal abstraction.
				 // We do this to make sure we're testing concurrency correctly, you should not do this!
				 // This is why we cannot create a single builder and share it across trials.
				final FSMBuilder<Event, State, Object, Object> builder = new FSMBuilder<>();
				// When the state argument is false, this transition records a firing and has a delay to ensure concurrency bugs are seen
				builder.transition(transition(type(State.class)).event(type(Event.class)).guard((ca, ea) -> !ca).next(type(State.class)).argument((ca, ea) -> {
					HConcurrent.wait(20);
					fired.add(ea);
					return true;
				}).build());
				// When the state arguemnt is true, we do nothing.
				builder.transition(transition(type(State.class)).event(type(Event.class)).guard((ca, ea) -> ca).next(type(State.class)).argument((ca, ea) -> true).build());
				tester = new FSMTester<>(builder.build(value(State.class, false), isSafe()));
				tester.assertState(value(State.class, false));
			}

			{ // Create a bunch of threads, and wait for them all to fire an event
				final CountDownLatch latch = new CountDownLatch(threads);
				// The event argument is the thread index.
				// Note that we create all the threads before we start any of them to try and create more chances for concurrency issues
				IntStream.range(0, threads).mapToObj(i -> new Thread(() -> {
					tester.fire(value(Event.class, i));
					latch.countDown();
				})).collect(Collectors.toList()).forEach(Thread::start);
				// Wait for all the threads to finish
				latch.await(1, TimeUnit.SECONDS);
			}

			// Make sure that we either saw (or didn't see) concurrency problems depending on whether we enable thread safety in the FSM builder
			if (isSafe()) HAssert.assertEquals(fired.toString(), 1, fired.size());
			else HAssert.assertTrue(fired.toString(), fired.size() > 1);
		}
	}
}
