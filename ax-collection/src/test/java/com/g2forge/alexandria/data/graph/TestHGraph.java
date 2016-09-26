package com.g2forge.alexandria.data.graph;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;

public class TestHGraph {
	@Test(expected = IllegalArgumentException.class)
	public void cyclic() {
		HGraph.toposort(HCollection.asList("A", "B"), n -> HCollection.asSet(new String(new char[] { (char) (('B' - n.charAt(0)) + 'A') })), true);
	}

	@Test
	public void simple() {
		Assert.assertEquals(HCollection.asList("A", "B"), HGraph.toposort(HCollection.asList("A", "B"), n -> "A".equals(n) ? HCollection.asSet("B") : HCollection.emptySet(), true));
		Assert.assertEquals(HCollection.asList("B", "A"), HGraph.toposort(HCollection.asList("A", "B"), n -> "A".equals(n) ? HCollection.asSet("B") : HCollection.emptySet(), false));
	}
}
