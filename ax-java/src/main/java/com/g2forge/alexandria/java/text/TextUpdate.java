package com.g2forge.alexandria.java.text;

import com.g2forge.alexandria.java.function.IFunction1;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class TextUpdate<O> {
	/** The offset in the input text of the region to replace. */
	protected final int offset;

	/** The length of the input text region to replace. May be {@code 0} to insert something. */
	protected final int length;

	/**
	 * A function from the region defined by {@link #getOffset()} and {@link #getLength()} to the value to replace it with. Depending on the consumer a
	 * {@code null} or {@code ""} return value can be used to delete text.
	 */
	protected final IFunction1<? super CharSequence, ? extends O> function;
}
