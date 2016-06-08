package com.g2forge.alexandria.generic.reflection.record.typedmap.implementations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.g2forge.alexandria.generic.record.IFieldType;
import com.g2forge.alexandria.generic.reflection.record.typedmap.IJavaTypedFieldType;
import com.g2forge.alexandria.generic.reflection.record.typedmap.IJavaTypedRecordType;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.java.IJavaUntype;
import com.g2forge.alexandria.generic.type.java.implementations.JavaClassType;

public class JavaTypedMapRecordType implements IJavaTypedRecordType<JavaTypedMapRecord> {
	protected final Collection<JavaTypedMapFieldType<?>> fieldTypes;
	
	/**
	 * @param fieldTypes
	 */
	public JavaTypedMapRecordType(final Collection<? extends IJavaTypedFieldType<JavaTypedMapRecord, ?>> fieldTypes) {
		this.fieldTypes = new ArrayList<>();
		if (fieldTypes != null) for (IJavaTypedFieldType<JavaTypedMapRecord, ?> fieldType : fieldTypes) {
			if (fieldType instanceof JavaTypedMapFieldType) this.fieldTypes.add((JavaTypedMapFieldType<?>) fieldType);
			else this.fieldTypes.add(new JavaTypedMapFieldType<>(fieldType));
		}
	}
	
	/**
	 * @param fieldTypes
	 */
	@SafeVarargs
	public JavaTypedMapRecordType(final IJavaTypedFieldType<JavaTypedMapRecord, ?>... fieldTypes) {
		this(Arrays.asList(fieldTypes));
	}

	@Override
	public JavaTypedMapRecord create() {
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
