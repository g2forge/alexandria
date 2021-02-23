package com.g2forge.alexandria.java.text;

import com.g2forge.alexandria.java.function.IFunction1;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class TextUpdate<O> {
	/**
	 * The offset in the input text of the region to replace.
	 * 
	 * @param The offset in the input text of the region to replace.
	 * @return The offset in the input text of the region to replace.
	 */
	protected final int offset;

	/**
	 * The length of the input text region to replace. May be {@code 0} to insert something.
	 * 
	 * @param The length of the input text region to replace.
	 * @return The length of the input text region to replace.
	 */
	protected final int length;

	/**
	 * A function from the contents of the region defined by {@link #getOffset()} and {@link #getLength()} to the value to replace it with. Depending on the
	 * consumer a {@code null} or {@code ""} return value can be used to delete text.
	 * 
	 * @param A function from the contents of this region to the value to replace it with.
	 * @return A function from the contents of this region to the value to replace it with.
	 */
	protected final IFunction1<? super CharSequence, ? extends O> function;
}
