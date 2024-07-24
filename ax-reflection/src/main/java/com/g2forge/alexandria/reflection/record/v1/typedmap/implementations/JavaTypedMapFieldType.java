package com.g2forge.alexandria.reflection.record.v1.typedmap.implementations;

import java.lang.reflect.Type;
import java.util.Objects;

import com.g2forge.alexandria.adt.record.v1.IField;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.java.HJavaType;
import com.g2forge.alexandria.generic.type.java.IJavaUntype;
import com.g2forge.alexandria.reflection.record.v1.typedmap.IJavaTypedFieldType;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JavaTypedMapFieldType<T> implements IJavaTypedFieldType<JavaTypedMapRecord, T> {
	protected final String name;

	protected final IJavaUntype type;

	public JavaTypedMapFieldType(IJavaTypedFieldType<JavaTypedMapRecord, T> fieldType) {
		this(fieldType.getName(), fieldType.getType());
	}

	public JavaTypedMapFieldType(String name, Class<T> type) {
		this(name, (Type) type);
	}

	public JavaTypedMapFieldType(String name, Type type) {
		this(name, HJavaType.toType(type, EmptyTypeEnvironment.create()));
	}

	@Override
	public IField<JavaTypedMapRecord, T> apply(final JavaTypedMapRecord input) {
		return new JavaTypedMapField<T>(this, input);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof JavaTypedMapFieldType)) return false;

		final JavaTypedMapFieldType<?> that = (JavaTypedMapFieldType<?>) obj;
		return Objects.equals(getName(), that.getName()) && Objects.equals(getType(), that.getType());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IJavaUntype getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName(), getType());
	}
}
