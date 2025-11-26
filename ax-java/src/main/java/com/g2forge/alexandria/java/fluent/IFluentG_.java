package com.g2forge.alexandria.java.fluent;

import java.util.function.Consumer;
import java.util.stream.Stream;

public interface IFluentG_<T> extends IFluent__<T> {
	public boolean isEmpty();

	public default boolean isNotEmpty() {
		return !isEmpty();
	}

	public Stream<T> toStream();

	public void visit(Consumer<? super T> consumer);
}
