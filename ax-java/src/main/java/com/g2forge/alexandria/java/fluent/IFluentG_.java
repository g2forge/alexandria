package com.g2forge.alexandria.java.fluent;

import java.util.function.Consumer;

public interface IFluentG_<T> extends IFluent__<T> {
	public boolean isEmpty();

	public void visit(Consumer<? super T> consumer);
}
