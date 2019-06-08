package com.g2forge.alexandria.java.function.cache;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.adt.tuple.ITuple1GS;
import com.g2forge.alexandria.java.adt.tuple.ITuple1G_;
import com.g2forge.alexandria.java.adt.tuple.implementations.Tuple1GSO;
import com.g2forge.alexandria.java.adt.tuple.implementations.Tuple1G_I;
import com.g2forge.alexandria.java.adt.tuple.implementations.Tuple1G_O;

import lombok.Data;

public class TestCachingSupplier {
	@Data
	public static class Value {
		protected final String value;
	}

	@Test
	public void lazy() {
		// We use an tuple to store the input (we don't have fields here) and another one inside it to control the definition of identity
		final ITuple1GS<ITuple1G_<Value>> input = new Tuple1GSO<>(null);
		// Count the number of times the supplier actually calls the function
		final ITuple1GS<Integer> count = new Tuple1GSO<>(0);
		final Supplier<String> supplier = new CachingSupplier<>(() -> input.get0(), tuple -> {
			count.set0(count.get0() + 1);
			return tuple.get0().getValue();
		});

		// For both object and system identities
		for (Function<Value, ITuple1G_<Value>> function : Arrays.<Function<Value, ITuple1G_<Value>>>asList(Tuple1G_O<Value>::new, Tuple1G_I<Value>::new)) {
			// Change the value a few times
			final String[] values = new String[] { null, "A", "A", "B", "B", "A" };
			for (int i = 0; i < values.length; i++) {
				final String value = values[i];
				// Wrap the new value in a tuple according to the function
				final ITuple1G_<Value> tuple = function.apply(new Value(value));
				// Compute whether this new tuple represents a change, taking into account what kind of identity function is in use
				final boolean change = (tuple instanceof Tuple1G_I) || (i == 0) || !Objects.equals(values[i - 1], value);
				input.set0(tuple);

				// We'll compare the invocation count relative to a base value, so that the tests below work across iterations
				final int expectedCount = count.get0() + (change ? 1 : 0);

				// Get the value a few times and make sure the count doesn't change unless it should
				for (int j = 0; j < 3; j++) {
					Assert.assertSame("Failed during iteration" + j, value, supplier.get());
					Assert.assertEquals("Failed during iteration" + j, expectedCount, count.get0().intValue());
				}
			}
		}
	}
}
