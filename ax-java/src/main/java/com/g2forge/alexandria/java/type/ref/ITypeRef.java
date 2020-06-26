package com.g2forge.alexandria.java.type.ref;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.stream.Stream;

import com.g2forge.alexandria.annotations.note.Note;
import com.g2forge.alexandria.annotations.note.NoteType;
import com.g2forge.alexandria.java.type.IDynamicType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Captures the relationship between the static type <code>T</code> and the runtime type {@link #getType()}. Due to the lack of proper generic support in the
 * Java reflection library, this interface is currently not statically type safe. As such implementors are responsible for type safety.
 *
 * @param <T> The (static) type of this type reference.
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
	 * @param <T> The (static) type of the resulting type reference.
	 * @param type The class to create a reference to.
	 * @return An instance of {@link ITypeRef} whose {@link #getType()} returns <code>type</code>.
	 */
	public static <T> ITypeRef<T> of(Class<T> type) {
		return new ClassTypeRef<>(type);
	}

	public default T cast(Object value) {
		return getErasedType().cast(value);
	}

	public default Stream<T> castIfInstance(Object value) {
		if (!isInstance(value)) return Stream.empty();
		return Stream.of(cast(value));
	}

	@Note(type = NoteType.TODO, value = "Properly implement erasure")
	public default Class<T> getErasedType() {
		final Type type = getType();
		if (type instanceof ParameterizedType) {
			@SuppressWarnings("unchecked")
			final Class<T> retVal = (Class<T>) ((ParameterizedType) type).getRawType();
			return retVal;
		}
		// TODO: Properly implement erasure here
		@SuppressWarnings("unchecked")
		final Class<T> klass = (Class<T>) type;
		return klass;
	}

	/**
	 * Get the dynamic (runtime) type of <code>T</code>.
	 * 
	 * @return The dynamic (runtime) type of <code>T</code>.
	 */
	public Type getType();

	@Note(type = NoteType.TODO, value = "Implementation is neither general to all dynamic types, nor generic-safe")
	public default boolean isAssignableFrom(IDynamicType<?> type) {
		return getErasedType().isAssignableFrom(((ITypeRef<?>) type).getErasedType());
	}

	@Note(type = NoteType.TODO, value = "Take generics into account when determining instanceOf, right now it's only checked erased type")
	public default boolean isInstance(Object value) {
		return getErasedType().isInstance(value);
	}
}
