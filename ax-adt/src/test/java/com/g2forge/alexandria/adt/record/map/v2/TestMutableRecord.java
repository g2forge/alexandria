package com.g2forge.alexandria.adt.record.map.v2;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.adt.record.v2.accessor.IFieldAccessor;
import com.g2forge.alexandria.adt.record.v2.accessor.MutableRecordAccessor;
import com.g2forge.alexandria.adt.record.v2.type.FieldType;
import com.g2forge.alexandria.adt.record.v2.type.IRecordType;
import com.g2forge.alexandria.adt.record.v2.type.RecordType;
import com.g2forge.alexandria.adt.record.v2.type.RecordType.RecordTypeBuilder;
import com.g2forge.alexandria.java.function.IFunction1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TestMutableRecord {
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MutableRecord {
		protected String field;
	}

	@Test
	public void mutableMap() {
		final String key = "key", value0 = "value0", value1 = "value1";

		final RecordTypeBuilder<Map<String, String>, Map<String, String>> recordTypeBuilder = RecordType.<Map<String, String>, Map<String, String>>builder().factory(LinkedHashMap::new).builder(IFunction1.identity());
		final FieldType<Map<String, String>, Map<String, String>, String> fieldType = FieldType.create(key);
		recordTypeBuilder.field(fieldType);
		final IRecordType<Map<String, String>, Map<String, String>> recordType = recordTypeBuilder.build();

		final MutableRecordAccessor<Map<String, String>> recordAccessor = new MutableRecordAccessor<>(recordType);
		final Map<String, String> record = recordAccessor.getRecord();
		final IFieldAccessor<? super Map<String, String>, String> field = recordAccessor.getField(fieldType);
		Assert.assertTrue(record.isEmpty());
		Assert.assertEquals(key, field.getType().getName());
		Assert.assertEquals(field, recordAccessor.getField(key));

		Assert.assertEquals(value0, field.set0(value0).get0());
		Assert.assertEquals(1, record.size());
		Assert.assertEquals(value0, record.get(key));

		Assert.assertEquals(value0, record.put(key, value1));
		Assert.assertEquals(value1, field.get0());
	}

	@Test
	public void mutableRecord() {
		final String name = "field", value0 = "value0", value1 = "value1";

		final RecordTypeBuilder<MutableRecord, MutableRecord> recordTypeBuilder = RecordType.<MutableRecord, MutableRecord>builder().factory(MutableRecord::new).builder(IFunction1.identity());
		final FieldType<MutableRecord, MutableRecord, String> fieldType = FieldType.create(MutableRecord::getField, MutableRecord::setField);
		recordTypeBuilder.field(fieldType);
		final IRecordType<MutableRecord, MutableRecord> recordType = recordTypeBuilder.build();

		final MutableRecordAccessor<MutableRecord> recordAccessor = new MutableRecordAccessor<>(recordType);
		final MutableRecord record = recordAccessor.getRecord();
		final IFieldAccessor<? super MutableRecord, String> field = recordAccessor.getField(fieldType);
		Assert.assertNull(record.field);
		Assert.assertEquals(name, field.getType().getName());
		Assert.assertEquals(field, recordAccessor.getField(name));

		Assert.assertEquals(value0, field.set0(value0).get0());
		Assert.assertEquals(value0, record.getField());

		record.setField(value1);
		Assert.assertEquals(value1, field.get0());
	}
}
