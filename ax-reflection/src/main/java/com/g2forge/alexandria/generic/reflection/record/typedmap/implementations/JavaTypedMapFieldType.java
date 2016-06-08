package com.g2forge.alexandria.generic.reflection.record.typedmap.implementations;

import java.lang.reflect.Type;

import com.g2forge.alexandria.generic.java.ObjectHelpers;
import com.g2forge.alexandria.generic.record.IField;
import com.g2forge.alexandria.generic.reflection.record.typedmap.IJavaTypedFieldType;
import com.g2forge.alexandria.generic.type.environment.implementations.EmptyTypeEnvironment;
import com.g2forge.alexandria.generic.type.java.IJavaUntype;
import com.g2forge.alexandria.generic.type.java.JavaTypeHelpers;

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
	
	/* @see java.lang.Object#equals(java.lang.Object) */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof JavaTypedMapFieldType)) return false;
		
		final JavaTypedMapFieldType<?> that = (JavaTypedMapFieldType<?>) obj;
		return ObjectHelpers.equals(getName(), that.getName()) && ObjectHelpers.equals(getType(), that.getType());
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
		return ObjectHelpers.hashCode(getName(), getType());
	}
	
	@Override
	public IField<JavaTypedMapRecord, T> map(final JavaTypedMapRecord input) {
		return new JavaTypedMapField<T>(this, input);
	}
}
