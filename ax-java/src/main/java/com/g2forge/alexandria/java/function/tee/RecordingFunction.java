package com.g2forge.alexandria.java.function.tee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import com.g2forge.alexandria.java.tuple.ITuple2G_;

import lombok.Getter;

public class RecordingFunction<I, O> extends TeeFunction<I, O> {
	@Getter
	protected final Collection<ITuple2G_<? extends I, ? extends O>> record;

	public RecordingFunction(Function<I, O> function) {
		this(function, new ArrayList<>());
	}

	private RecordingFunction(Function<I, O> function, Collection<ITuple2G_<? extends I, ? extends O>> record) {
		super(function, record::add);
		this.record = record;
	}
}
