package com.g2forge.alexandria.adt.record.map.v2;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.adt.record.map.v2.TestImmutableRecord.ImmutableRecord.ImmutableRecordBuilder;
import com.g2forge.alexandria.adt.record.v2.accessor.IFieldAccessor;
import com.g2forge.alexandria.adt.record.v2.accessor.MutableRecordAccessor;
import com.g2forge.alexandria.adt.record.v2.type.FieldType;
import com.g2forge.alexandria.adt.record.v2.type.IRecordType;
import com.g2forge.alexandria.adt.record.v2.type.RecordType;
import com.g2forge.alexandria.adt.record.v2.type.RecordType.RecordTypeBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class TestImmutableRecord {
	@Data
	@Builder
	@AllArgsConstructor
	public static class ImmutableRecord {
		protected final String field;
	}

	@Test
	public void immutableMap() {
		final String key = "key", value0 = "value0", value1 = "value1";

		final RecordTypeBuilder<Map<String, String>, Map<String, String>> recordTypeBuilder = RecordType.<Map<String, String>, Map<String, String>>builder().factory(LinkedHashMap::new).builder(Collections::unmodifiableMap);
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
	public void immutableRecord() {
		final String name = "field", value0 = "value0", value1 = "value1";

		final RecordTypeBuilder<ImmutableRecord, ImmutableRecord.ImmutableRecordBuilder> recordTypeBuilder = RecordType.<ImmutableRecord, ImmutableRecord.ImmutableRecordBuilder>builder().factory(ImmutableRecord::builder).builder(ImmutableRecord.ImmutableRecordBuilder::build);
		final FieldType<ImmutableRecord, ImmutableRecord.ImmutableRecordBuilder, String> fieldType = FieldType.create(ImmutableRecord::getField, ImmutableRecord.ImmutableRecordBuilder::field);
		recordTypeBuilder.field(fieldType);
		final IRecordType<ImmutableRecord, ImmutableRecord.ImmutableRecordBuilder> recordType = recordTypeBuilder.build();

		final ImmutableRecordBuilder builder = recordType.getFactory().get();
		recordType.getField(name).getSetter();

		final MutableRecordAccessor<ImmutableRecord> recordAccessor = new MutableRecordAccessor<>(recordType);
		final ImmutableRecord record = recordAccessor.getRecord();
		final IFieldAccessor<? super ImmutableRecord, String> field = recordAccessor.getField(fieldType);
		Assert.assertNull(record.field);
		Assert.assertEquals(name, field.getType().getName());
		Assert.assertEquals(field, recordAccessor.getFields().get(name));

		Assert.assertEquals(value0, field.set0(value0).get0());
		Assert.assertEquals(value0, record.getField());

		record.setField(value1);
		Assert.assertEquals(value1, field.get0());
	}
}
