package com.g2forge.alexandria.generic.reflection.record.reflected;

import java.util.Objects;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.adt.collection.CollectionHelpers;
import com.g2forge.alexandria.generic.record.IField;
import com.g2forge.alexandria.generic.reflection.record.reflected.implementations.ReflectedRecordType;

public class TestReflectedRecord {
	public static class Record {
		protected String field0;
		
		public Record() {}
		
		/**
		 * @param field0
		 */
		public Record(final String field0) {
			this.field0 = field0;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object) */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (!(obj instanceof Record)) return false;
			
			final Record that = (Record) obj;
			if (!Objects.equals(getField0(), that.getField0())) return false;
			return true;
		}
		
		/**
		 * @return the field0
		 */
		public String getField0() {
			return field0;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode() */
		@Override
		public int hashCode() {
			return Objects.hashCode(getField0());
		}
		
		/**
		 * @param field0 the field0 to set
		 */
		public void setField0(String field0) {
			this.field0 = field0;
		}
	}
	
	@Test
	public void test() {
		final IReflectedRecordType<Record> recordType = new ReflectedRecordType<>(Record.class, null);
		
		final String expected = "foo";
		final Record constructed = new Record(expected);
		final IField<Record, ?> field = CollectionHelpers.getOne(recordType.getFields()).apply(constructed);
		Assert.assertEquals(expected, field.getAccessor().get0());
		
		final Record created = recordType.create();
		created.setField0(expected);
		Assert.assertEquals(constructed, created);
	}
}
