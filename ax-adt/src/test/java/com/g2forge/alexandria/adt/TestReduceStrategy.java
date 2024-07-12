package com.g2forge.alexandria.adt;

import org.junit.Test;

import com.g2forge.alexandria.test.HAssert;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public class TestReduceStrategy {
	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	public static class Record {
		protected final String field;
	}

	@Test
	public void none() {
		final Record.RecordBuilder builder = Record.builder();
		ReduceStrategy.NullIsNone.reduce(new Record(null), new Record(null), Record::getField, builder::field, null);
		HAssert.assertNull(builder.build().getField());
	}

	@Test
	public void a() {
		final Record.RecordBuilder builder = Record.builder();
		ReduceStrategy.NullIsNone.reduce(new Record("A"), new Record(null), Record::getField, builder::field, null);
		HAssert.assertEquals("A", builder.build().getField());
	}

	@Test
	public void b() {
		final Record.RecordBuilder builder = Record.builder();
		ReduceStrategy.NullIsNone.reduce(new Record(null), new Record("B"), Record::getField, builder::field, null);
		HAssert.assertEquals("B", builder.build().getField());
	}

	@Test
	public void merge() {
		final Record.RecordBuilder builder = Record.builder();
		final Record a = new Record("A");
		final Record b = new Record("B");
		HAssert.assertThrows(IllegalArgumentException.class, () -> ReduceStrategy.NullIsNone.reduce(a, b, Record::getField, builder::field, null));
	}
}
