package com.g2forge.alexandria.reflection.record.typedmap.implementations;

import java.lang.reflect.Type;
import java.util.Objects;

import com.g2forge.alexandria.generic.record.IField;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.java.IJavaUntype;
import com.g2forge.alexandria.generic.type.java.JavaTypeHelpers;
import com.g2forge.alexandria.reflection.record.typedmap.IJavaTypedFieldType;

public class JavaTypedMapFieldType<T> implements IJavaTypedFieldType<JavaTypedMapRecord, T> {
	protected final String name;

	protected final IJavaUntype type;

	/**
	 * @param fieldType
	 */
	public JavaTypedMapFieldType(IJavaTypedFieldType<JavaTypedMapRecord, T> fieldType) {
		this(fieldType.getName(), fieldType.getType());
	}

	/**
	 * @param name
	 * @param type
	 */
	public JavaTypedMapFieldType(String name, Class<T> type) {
		this(name, (Type) type);
	}

	/**
	 * @param name
	 * @param type
	 */
	public JavaTypedMapFieldType(String name, IJavaUntype type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * @param name
	 * @param type
	 */
	public JavaTypedMapFieldType(String name, Type type) {
		this(name, JavaTypeHelpers.toType(type, EmptyTypeEnvironment.create()));
	}

	@Override
	public IField<JavaTypedMapRecord, T> apply(final JavaTypedMapRecord input) {
		return new JavaTypedMapField<T>(this, input);
	}

	/* @see java.lang.Object#equals(java.lang.Object) */
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

	/* @see java.lang.Object#hashCode() */
	@Override
	public int hashCode() {
		Object[] objects = { getName(), getType() };
		return Objects.hash(objects);
	}
}
