package com.g2forge.alexandria.java.adt.compare;

import java.util.Comparator;

import org.junit.Assert;
import org.junit.Test;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public class TestChainedComparator {
	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	public static class Record {
		protected final String a;

		protected final String b;
	}

	@Test
	public void basic() {
		final Record zero = new Record("0", "0");
		final Record one = new Record("0", "1");
		final Record two = new Record("0", "2");
		final Comparator<Record> comparator = HComparator.createRecordComparator(Record::getA, Record::getB);

		Assert.assertTrue(comparator.compare(zero, zero) == 0);
		Assert.assertTrue(comparator.compare(zero, one) < 0);
		Assert.assertTrue(comparator.compare(zero, two) < 0);

		Assert.assertTrue(comparator.compare(one, zero) > 0);
		Assert.assertTrue(comparator.compare(one, one) == 0);
		Assert.assertTrue(comparator.compare(one, two) < 0);

		Assert.assertTrue(comparator.compare(two, zero) > 0);
		Assert.assertTrue(comparator.compare(two, one) > 0);
		Assert.assertTrue(comparator.compare(two, two) == 0);
	}

	@Test
	public void priority() {
		final Record zero = new Record("0", "2");
		final Record one = new Record("1", "1");
		final Record two = new Record("2", "0");
		final Comparator<Record> comparator = HComparator.createRecordComparator(Record::getA, Record::getB);

		Assert.assertTrue(comparator.compare(zero, zero) == 0);
		Assert.assertTrue(comparator.compare(zero, one) < 0);
		Assert.assertTrue(comparator.compare(zero, two) < 0);

		Assert.assertTrue(comparator.compare(one, zero) > 0);
		Assert.assertTrue(comparator.compare(one, one) == 0);
		Assert.assertTrue(comparator.compare(one, two) < 0);

		Assert.assertTrue(comparator.compare(two, zero) > 0);
		Assert.assertTrue(comparator.compare(two, one) > 0);
		Assert.assertTrue(comparator.compare(two, two) == 0);
	}

	@Test
	public void reversed() {
		final Record zero = new Record("0", "2");
		final Record one = new Record("1", "1");
		final Record two = new Record("2", "0");
		final Comparator<Record> comparator = HComparator.createRecordComparator(Record::getB, Record::getA);

		Assert.assertTrue(comparator.compare(zero, zero) == 0);
		Assert.assertTrue(comparator.compare(zero, one) > 0);
		Assert.assertTrue(comparator.compare(zero, two) > 0);

		Assert.assertTrue(comparator.compare(one, zero) < 0);
		Assert.assertTrue(comparator.compare(one, one) == 0);
		Assert.assertTrue(comparator.compare(one, two) > 0);

		Assert.assertTrue(comparator.compare(two, zero) < 0);
		Assert.assertTrue(comparator.compare(two, one) < 0);
		Assert.assertTrue(comparator.compare(two, two) == 0);
	}
}
