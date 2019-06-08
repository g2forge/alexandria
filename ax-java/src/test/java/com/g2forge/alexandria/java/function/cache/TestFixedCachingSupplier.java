package com.g2forge.alexandria.java.function.cache;

import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.adt.tuple.ITuple1GS;
import com.g2forge.alexandria.java.adt.tuple.implementations.Tuple1GSO;

public class TestFixedCachingSupplier {
	@Test
	public void lazy() {
		final ITuple1GS<Integer> count = new Tuple1GSO<>(0);
		final String string = "Hello, World!";
		final Supplier<String> supplier = new FixedCachingSupplier<>(() -> {
			count.set0(count.get0() + 1);
			return string;
		});
		Assert.assertEquals(0, count.get0().intValue());
		Assert.assertEquals(string, supplier.get());
		Assert.assertEquals(1, count.get0().intValue());
		Assert.assertEquals(string, supplier.get());
		Assert.assertEquals(1, count.get0().intValue());
	}
}
