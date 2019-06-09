package com.g2forge.alexandria.adt.record.v2.accessor;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.g2forge.alexandria.adt.record.v2.type.IFieldType;
import com.g2forge.alexandria.adt.record.v2.type.IRecordType;
import com.g2forge.alexandria.java.adt.identity.IIdentity;
import com.g2forge.alexandria.java.function.IFunction1;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class MutableRecordAccessor<Record> implements IRecordAccessor<Record> {
	protected final IRecordType<Record, Record> type;

	protected final Record record;

	protected final IIdentity<? super Record> identity;

	@Getter(lazy = true)
	private final Collection<IFieldAccessor<? super Record, ?>> fields = getType().getFields().stream().map(this::createField).collect(Collectors.toList());

	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private final Map<String, ? extends IFieldAccessor<? super Record, ?>> fieldMap = getFields().stream().collect(Collectors.toMap(field -> field.getType().getName(), IFunction1.identity()));

	public MutableRecordAccessor(IRecordType<Record, Record> type) {
		this(type, type.getFactory().get());
	}

	public MutableRecordAccessor(IRecordType<Record, Record> type, Record record) {
		this.type = type;
		this.record = record;
		this.identity = IIdentity.same();
		if (!getType().isMutable()) throw new IllegalArgumentException();
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
		if (!getIdentity().equals(getRecord(), that.getRecord())) return false;
		return Objects.equals(getType(), that.getType());
	}

	public <Field> IFieldAccessor<? super Record, Field> getField(IFieldType<? super Record, ? super Record, Field> fieldType) {
		return getField(fieldType.getName()).as(fieldType);
	}

	public IFieldAccessor<? super Record, ?> getField(String name) {
		return getFieldMap().get(name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getType(), getIdentity().hashCode(getRecord()));
	}
}
