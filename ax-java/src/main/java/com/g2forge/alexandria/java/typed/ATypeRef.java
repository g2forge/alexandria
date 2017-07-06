package com.g2forge.alexandria.java.typed;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.g2forge.alexandria.java.core.error.ReflectedCodeError;

import lombok.Getter;
import lombok.ToString;

/**
 * Abstract implementation of {@link ITypeRef} used to statically capture a parameterized (generic) Java type. If you the type <code>T</code> is not parameterized, you may
 * prefer {@link ITypeRef#of(Class)} as being simpler. To use this class one should create an anonymous class <code>new ATypeRef<List<String>>(){}</code>. By constructing the
 * anonymous class we are able to use the Java reflection API in {@link #TypeRef()} to ensure that {@link #getType()} returns <code>List&lt;String&gt;</code> as an instance of
 * {@link ParameterizedType}.
 * 
 * @param <T>
 *            The (static) Java type to capture.
 */
@ToString
public abstract class ATypeRef<T> extends ATypeRefIdentity<T> {
	@Getter
	protected final Type type;

	protected ATypeRef() {
		// Get the actual class (the anonymous class), and then get it's superclass, which should be this type
		final Type superclass = getClass().getGenericSuperclass();
		// Check that our expectations are being met
		if (!(superclass instanceof ParameterizedType)) throw new ReflectedCodeError("Super class is not a parameterized type!");
		final ParameterizedType parameterized = (ParameterizedType) superclass;
		if (!parameterized.getRawType().equals(ATypeRef.class)) throw new ReflectedCodeError("Super class is not " + ATypeRef.class + "!");

		// Get the dynamic type of <T>, which is the 0 type argument
		type = parameterized.getActualTypeArguments()[0];
	}
}
