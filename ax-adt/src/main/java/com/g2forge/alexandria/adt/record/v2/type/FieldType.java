package com.g2forge.alexandria.adt.record.v2.type;

import java.util.Map;

import com.g2forge.alexandria.analysis.ISerializableConsumer2;
import com.g2forge.alexandria.analysis.ISerializableFunction1;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@lombok.Builder
@AllArgsConstructor
public class FieldType<Get, Set, Field> implements IFieldType<Get, Set, Field> {
	@AllArgsConstructor
	public static class BoundFieldTypeBuilder<Get, Set, Field> extends FieldTypeBuilder<Get, Set, Field> {
		@Getter
		protected final RecordType.RecordTypeBuilder<Get, Set> record;

		public FieldType<Get, Set, Field> build() {
			final FieldType<Get, Set, Field> retVal = super.build();
			getRecord().field(retVal);
			return retVal;
		}
	}

	public static <Get, Set, Field> FieldType<Get, Set, Field> create(ISerializableFunction1<? super Get, ? extends Field> getter, ISerializableConsumer2<? super Set, ? super Field> setter) {
		final String getPath = getter.asMethodAnalyzer().getPath();
		final String setPath = setter.asMethodAnalyzer().getPath();
		if (!getPath.equals(setPath)) throw new IllegalArgumentException(String.format("Getter (\"%1$s\") and setter (\"%1$s\") do not match!", getPath, setPath));
		return new FieldType<>(getPath, getter, setter);
	}

	public static <Key, Value> FieldType<Map<Key, Value>, Map<Key, Value>, Value> create(final Key key) {
		final FieldTypeBuilder<Map<Key, Value>, Map<Key, Value>, Value> retVal = FieldType.builder();
		retVal.name(key.toString());
		retVal.getter(map -> map.get(key));
		retVal.setter((map, value) -> map.put(key, value));
		return retVal.build();
	}

	protected final String name;

	protected final IFunction1<? super Get, ? extends Field> getter;

	protected final IConsumer2<? super Set, ? super Field> setter;
}
