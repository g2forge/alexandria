package com.g2forge.alexandria.java.adt.compare;

import org.junit.Assert;
import org.junit.Test;

public class TestMappedComparator {
	@Test
	public void test() {
		Assert.assertEquals(0, new MappedComparator<>(String::length, ComparableComparator.create()).compare("a", "b"));
		Assert.assertEquals(-1, new MappedComparator<>(String::length, ComparableComparator.create()).compare("a", "b0"));
		Assert.assertEquals(1, new MappedComparator<>(String::length, ComparableComparator.create()).compare("001", "10"));
	}
}
