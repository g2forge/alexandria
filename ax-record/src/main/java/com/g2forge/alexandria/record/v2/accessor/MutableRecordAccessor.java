package com.g2forge.alexandria.record.v2.accessor;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.record.v2.type.IFieldType;
import com.g2forge.alexandria.record.v2.type.IRecordType;

import lombok.Data;
import lombok.Getter;

@Data
public class MutableRecordAccessor<Record> implements IRecordAccessor<Record> {
	protected final IRecordType<Record, Record> type;

	protected final Record record;

	@Getter(lazy = true)
	private final Map<String, ? extends IFieldAccessor<? super Record, ?>> fields = computeFields();

	public MutableRecordAccessor(IRecordType<Record, Record> type) {
		this.type = type;
		this.record = getType().getFactory().create();
		if (!getType().isMutable()) throw new IllegalArgumentException();
	}

	protected Map<String, ? extends IFieldAccessor<? super Record, ?>> computeFields() {
		return getType().getFields().stream().map(this::createField).collect(Collectors.toMap(fieldAccessor -> fieldAccessor.getType().getName(), IFunction1.identity()));
	}

	protected IFieldAccessor<? super Record, ?> createField(IFieldType<? super Record, ? super Record, ?> fieldType) {
		return new MutableFieldAccessor<>(fieldType, this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		if (getClass() != o.getClass()) return false;

		final MutableRecordAccessor<?> that = (MutableRecordAccessor<?>) o;
		if (getRecord() != that.getRecord()) return false;
		return Objects.equals(getType(), that.getType());
	}

	@Override
	public <Field> IFieldAccessor<? super Record, Field> getField(IFieldType<? super Record, ? super Record, Field> fieldType) {
		return getFields().values().stream().map(accessor -> accessor.as(fieldType)).filter(Objects::nonNull).findAny().get();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getType(), System.identityHashCode(getRecord()));
	}
}
