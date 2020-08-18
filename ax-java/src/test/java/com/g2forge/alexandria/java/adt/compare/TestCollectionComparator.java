package com.g2forge.alexandria.java.adt.compare;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.math.HMath;
import com.g2forge.alexandria.java.core.math.Sign;

public class TestCollectionComparator {
	@Test
	public void equalsA() {
		Assert.assertEquals(0, getComparator().compare(HCollection.asList("a"), HCollection.asList("a")));
	}

	@Test
	public void equalsAB() {
		Assert.assertEquals(0, getComparator().compare(HCollection.asList("a", "b"), HCollection.asList("a", "b")));
	}

	@Test
	public void equalsStringB() {
		Assert.assertEquals(0, getComparator().compare(HCollection.asList("string", "b"), HCollection.asList("string", "b")));
	}

	protected CollectionComparator<String, List<String>> getComparator() {
		return new CollectionComparator<String, List<String>>(ComparableComparator.create());
	}

	@Test
	public void greaterAB() {
		Assert.assertEquals(1, getComparator().compare(HCollection.asList("a", "b"), HCollection.asList("a")));
	}

	@Test
	public void lessAB() {
		Assert.assertEquals(-1, getComparator().compare(HCollection.asList("a"), HCollection.asList("a", "some long string")));
	}

	@Test
	public void lessString() {
		Assert.assertEquals(Sign.Negative, HMath.sign(getComparator().compare(HCollection.asList("a"), HCollection.asList("string"))));
	}
}
