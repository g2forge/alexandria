package com.g2forge.alexandria.java.type;

import java.util.Optional;
import java.util.stream.Stream;

import com.g2forge.alexandria.annotations.note.Note;
import com.g2forge.alexandria.annotations.note.NoteType;
import com.g2forge.alexandria.java.core.error.NotYetImplementedError;

public interface IDynamicType<T> {
	public T cast(Object value);

	public default Stream<T> castIfInstance(Object value) {
		if (!isInstance(value)) return Stream.empty();
		return Stream.of(cast(value));
	}

	public default Optional<T> castIfInstanceOptional(Object value) {
		if (!isInstance(value)) return Optional.empty();
		return Optional.of(cast(value));
	}

	@Note(type = NoteType.TODO, value = "All implementations of this method need to be implemented in a generic-safe manner")
	public default boolean isAssignableFrom(IDynamicType<?> type) {
		throw new NotYetImplementedError();
	}

	public boolean isInstance(Object value);
}
