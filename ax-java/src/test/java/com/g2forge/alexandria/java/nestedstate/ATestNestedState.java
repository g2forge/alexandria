package com.g2forge.alexandria.java.nestedstate;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.annotations.note.Note;
import com.g2forge.alexandria.annotations.note.NoteType;
import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.function.IThrowRunnable;

public abstract class ATestNestedState {
	@Note(type = NoteType.TODO, value = "Find a way to integerate this with ax-test")
	public static <T extends Throwable> void assertException(Class<? extends Throwable> type, String message, IThrowRunnable<T> operation) throws T {
		try {
			operation.run();
		} catch (Throwable throwable) {
			if (type.isInstance(throwable)) {
				if (message != null) Assert.assertEquals(message, throwable.getMessage());
				return;
			} else throw throwable;
		}
		Assert.fail(String.format("Expected exception of type \"%1$s\" was not thrown!", type.getName()));
	}

	protected abstract <T> void assertEmpty(INestedState<T> state);

	protected abstract <T> INestedState<T> create();

	@Test
	public void test() {
		final INestedState<Integer> state = create();
		final ICloseableNestedState<Integer> closeable = state instanceof ICloseableNestedState ? (ICloseableNestedState<Integer>) state : null;

		assertEmpty(state);
		try (final ICloseable context = state.open(1)) {
			Assert.assertEquals(Integer.valueOf(1), state.get());
			if (closeable != null) assertException(IllegalStateException.class, null, () -> closeable.close(2));
		}
		assertEmpty(state);
	}
}
