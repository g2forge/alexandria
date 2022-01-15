package com.g2forge.alexandria.adt.graph.v1;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;

public class TestHGraph {
	@Test(expected = IllegalArgumentException.class)
	public void cyclic() {
		HGraph.toposort(HCollection.asList("A", "B"), n -> HCollection.asSet(new String(new char[] { (char) (('B' - n.charAt(0)) + 'A') })), true);
	}

	@Test
	public void deterministic() {
		for (int i = 0; i < 100; i++) {
			final Object a = new Object();
			final Object b = new Object();
			Assert.assertEquals(HCollection.asList(a, b), HGraph.toposort(HCollection.asList(a, b), n -> HCollection.emptySet(), true));
		}
	}

	@Test
	public void simple() {
		Assert.assertEquals(HCollection.asList("A", "B"), HGraph.toposort(HCollection.asList("A", "B"), n -> "A".equals(n) ? HCollection.asSet("B") : HCollection.emptySet(), true));
		Assert.assertEquals(HCollection.asList("B", "A"), HGraph.toposort(HCollection.asList("A", "B"), n -> "A".equals(n) ? HCollection.asSet("B") : HCollection.emptySet(), false));
	}
}
