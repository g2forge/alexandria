package com.g2forge.alexandria.java.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import com.g2forge.alexandria.java.tuple.ITuple2G_;
import com.g2forge.alexandria.java.tuple.Tuple2GS;

import lombok.Data;

@Data
public class RecordingFunction<I, O> implements Function<I, O> {
	protected final Function<I, O> function;

	protected final Collection<ITuple2G_<I, O>> record = new ArrayList<>();

	@Override
	public O apply(I input) {
		final O retVal = function.apply(input);
		record.add(new Tuple2GS<>(input, retVal));
		return retVal;
	}
}
