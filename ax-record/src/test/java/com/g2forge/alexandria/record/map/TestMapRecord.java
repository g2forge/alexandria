package com.g2forge.alexandria.record.map;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.tuple.ITuple1GS;
import com.g2forge.alexandria.record.map.implementations.MapFieldType;
import com.g2forge.alexandria.record.map.implementations.MapRecord;
import com.g2forge.alexandria.record.map.implementations.MapRecordType;

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
