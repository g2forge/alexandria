package com.g2forge.alexandria.generic.reflection.record.typedmap;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.generic.java.tuple.ITuple1GS;
import com.g2forge.alexandria.generic.reflection.record.typedmap.implementations.JavaTypedMapFieldType;
import com.g2forge.alexandria.generic.reflection.record.typedmap.implementations.JavaTypedMapRecord;
import com.g2forge.alexandria.generic.reflection.record.typedmap.implementations.JavaTypedMapRecordType;

public class TestTypedMapRecord {
	@Test
	public void test() {
		final IJavaTypedFieldType<JavaTypedMapRecord, String> fieldType = new JavaTypedMapFieldType<>("field0", String.class);
		final IJavaTypedRecordType<JavaTypedMapRecord> recordType = new JavaTypedMapRecordType(fieldType);
		final JavaTypedMapRecord record = recordType.create();
		
		String value = "foo";
		final ITuple1GS<String> field = fieldType.map(record).getAccessor();
		Assert.assertNull(field.swap0(value));
		Assert.assertEquals(value, field.get0());
	}
}
