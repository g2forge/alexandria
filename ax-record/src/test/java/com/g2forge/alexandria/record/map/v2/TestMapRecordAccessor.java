package com.g2forge.alexandria.record.map.v2;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.record.v2.FieldType;
import com.g2forge.alexandria.record.v2.IRecordType;
import com.g2forge.alexandria.record.v2.MutableRecordAccessor;
import com.g2forge.alexandria.record.v2.RecordType;
import com.g2forge.alexandria.record.v2.RecordType.RecordTypeBuilder;

public class TestMapRecordAccessor {
	@Test
	public void test() {
		final RecordTypeBuilder<Map<String, String>, Map<String, String>> recordTypeBuilder = RecordType.<Map<String, String>, Map<String, String>>builder();
		recordTypeBuilder.factory(LinkedHashMap::new).builder(IFunction1.identity());
		final FieldType<Map<String, String>, Map<String, String>, String> fooFieldType = FieldType.create("foo");
		recordTypeBuilder.field(fooFieldType);
		final IRecordType<Map<String, String>, Map<String, String>> recordType = recordTypeBuilder.build();
		new MutableRecordAccessor<>(recordType).getFields().get("foo").set0("Thingy");

		// TODO: Make it easier to build map fields
		// TODO: MutableXXXAccessor classes shouldn't be based on maps
	}
}
