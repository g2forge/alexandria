package com.g2forge.alexandria.record.v2.type;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.core.iface.IFactory;
import com.g2forge.alexandria.java.function.IFunction1;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
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

	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private final Map<String, ? extends IFieldType<? super Record, ? super Builder, ?>> fieldMap = getFields().stream().collect(Collectors.toMap(IFieldType::getName, IFunction1.identity()));

	protected final IFactory<? extends Builder> factory;

	protected final IFunction1<? super Builder, ? extends Record> builder;

	@Singular
	protected final Collection<IFieldType<? super Record, ? super Builder, ?>> fields;

	public IFieldType<? super Record, ? super Builder, ?> getField(String name) {
		return getFieldMap().get(name);
	}
}
