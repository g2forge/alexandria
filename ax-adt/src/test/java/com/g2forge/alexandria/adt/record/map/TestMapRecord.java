package com.g2forge.alexandria.adt.record.map;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.adt.record.v1.map.MapFieldType;
import com.g2forge.alexandria.adt.record.v1.map.MapRecord;
import com.g2forge.alexandria.adt.record.v1.map.MapRecordType;
import com.g2forge.alexandria.java.adt.tuple.ITuple1GS;

public class TestMapRecord {
	@Test
	public void test() {
		final MapFieldType<String> fieldType = new MapFieldType<>("field0");
		final MapRecordType recordType = new MapRecordType(fieldType);
		final MapRecord record = recordType.get();

		String value = "foo";
		final ITuple1GS<String> field = fieldType.apply(record).getAccessor();
		Assert.assertNull(field.swap0(value));
		Assert.assertEquals(value, field.get0());
	}
}
