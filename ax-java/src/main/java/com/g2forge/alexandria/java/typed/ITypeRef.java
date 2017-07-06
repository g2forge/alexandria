package com.g2forge.alexandria.java.typed;

import java.lang.reflect.Type;

import com.g2forge.alexandria.annotations.TODO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Captures the relationship between the static type <code>T</code> and the runtime type {@link #getType()}. Due to the lack of proper generic support in the Java reflection
 * library, this interface is currently not statically type safe. As such implementors are responsible for type safety.
 *
 * @param <T>
 *            The (static) type of this type reference.
 */
@FunctionalInterface
public interface ITypeRef<T> extends IDynamicType<T> {
	@RequiredArgsConstructor
	@ToString
	@Getter
	public static class ClassTypeRef<T> extends ATypeRefIdentity<T> {
		protected final Class<T> type;
	}

	/**
	 * Construct an instance of {@link ITypeRef} for a Java {@link Class}. This is most useful for non-parameterized classes.
	 * 
	 * @param type
	 *            The class to create a reference to.
	 * @return An instance of {@link ITypeRef} whose {@link #getType()} returns <code>type</code>.
	 */
	public static <T> ITypeRef<T> of(Class<T> type) {
		return new ClassTypeRef<>(type);
	}

	public default T cast(Object value) {
		return getErasedType().cast(value);
	}

	@TODO("Properly implement erasure")
	public default Class<T> getErasedType() {
		// TODO: Properly implement erasure here
		@SuppressWarnings("unchecked")
		final Class<T> klass = (Class<T>) getType();
		return klass;
	}

	/**
	 * Get the dynamic (runtime) type of <code>T</code>.
	 * 
	 * @return The dynamic (runtime) type of <code>T</code>.
	 */
	public Type getType();

	@TODO("Implementation is neither general to all dynamic types, nor generic-safe")
	public default boolean isAssignableFrom(IDynamicType<?> type) {
		return getErasedType().isAssignableFrom(((ITypeRef<?>) type).getErasedType());
	}

	public default boolean isInstance(Object value) {
		return getErasedType().isInstance(value);
	}
}
