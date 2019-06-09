package com.g2forge.alexandria.java.type.ref;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.g2forge.alexandria.java.reflect.HReflection;

import lombok.Getter;
import lombok.ToString;

/**
 * Abstract implementation of {@link com.g2forge.alexandria.java.type.ref.ITypeRef} used to statically capture a parameterized (generic) Java type. If you the type
 * <code>T</code> is not parameterized, you may prefer {@link com.g2forge.alexandria.java.type.ref.ITypeRef#of(Class)} as being simpler. To use this class one
 * should create an anonymous class <code>new ATypeRef&lt;List&lt;String&gt;&gt;(){}</code>. By constructing the anonymous class we are able to use the Java
 * reflection API in {@link #ATypeRef()} to ensure that {@link #getType()} returns <code>List&lt;String&gt;</code> as an instance of {@link ParameterizedType}.
 * 
 * @param <T> The (static) Java type to capture.
 */
@ToString
public abstract class ATypeRef<T> extends ATypeRefIdentity<T> {
	@Getter
	protected final Type type;

	protected ATypeRef() {
		this.type = HReflection.getParentTypeArgument(this, ATypeRef.class, 0);
	}
}
