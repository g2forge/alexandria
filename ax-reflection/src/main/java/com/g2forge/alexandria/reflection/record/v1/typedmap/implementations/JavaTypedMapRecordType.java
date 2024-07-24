package com.g2forge.alexandria.reflection.record.v1.typedmap.implementations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.g2forge.alexandria.adt.record.v1.IFieldType;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.java.IJavaUntype;
import com.g2forge.alexandria.generic.type.java.type.implementations.JavaClassType;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.reflection.record.v1.typedmap.IJavaTypedFieldType;
import com.g2forge.alexandria.reflection.record.v1.typedmap.IJavaTypedRecordType;

public class JavaTypedMapRecordType implements IJavaTypedRecordType<JavaTypedMapRecord> {
	protected final Collection<JavaTypedMapFieldType<?>> fieldTypes;

	public JavaTypedMapRecordType(final Collection<? extends IJavaTypedFieldType<JavaTypedMapRecord, ?>> fieldTypes) {
		this.fieldTypes = new ArrayList<>();
		if (fieldTypes != null) for (IJavaTypedFieldType<JavaTypedMapRecord, ?> fieldType : fieldTypes) {
			if (fieldType instanceof JavaTypedMapFieldType) this.fieldTypes.add((JavaTypedMapFieldType<?>) fieldType);
			else this.fieldTypes.add(new JavaTypedMapFieldType<>(fieldType));
		}
	}

	@SafeVarargs
	public JavaTypedMapRecordType(final IJavaTypedFieldType<JavaTypedMapRecord, ?>... fieldTypes) {
		this(HCollection.asList(fieldTypes));
	}

	@Override
	public JavaTypedMapRecord get() {
		return new JavaTypedMapRecord();
	}

	@Override
	public Collection<? extends IFieldType<JavaTypedMapRecord, ?>> getFields() {
		return Collections.unmodifiableCollection(fieldTypes);
	}

	@Override
	public IJavaUntype getType() {
		return new JavaClassType(JavaTypedMapRecord.class, EmptyTypeEnvironment.create());
	}
}
