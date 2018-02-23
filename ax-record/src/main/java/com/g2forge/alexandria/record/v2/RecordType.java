package com.g2forge.alexandria.record.v2;

import java.util.Collection;

import com.g2forge.alexandria.java.core.iface.IFactory;
import com.g2forge.alexandria.java.function.IFunction1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Singular;

@Data
@lombok.Builder
@AllArgsConstructor
public class RecordType<Record, Builder> implements IRecordType<Record, Builder> {
	public static class RecordTypeBuilder<Record, Builder> {
		public <Field> FieldType.FieldTypeBuilder<Record, Builder, Field> createField() {
			return new FieldType.BoundFieldTypeBuilder<>(this);
		}
	}

	protected final IFactory<? extends Builder> factory;

	protected final IFunction1<? super Builder, ? extends Record> builder;

	@Singular
	protected final Collection<IFieldType<? super Record, ? super Builder, ?>> fields;
}
