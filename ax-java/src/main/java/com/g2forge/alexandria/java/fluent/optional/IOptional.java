package com.g2forge.alexandria.java.fluent.optional;

import java.util.function.Function;
import java.util.function.Predicate;

import com.g2forge.alexandria.java.fluent.IFluent1_;
import com.g2forge.alexandria.java.fluent.IFluent_0;
import com.g2forge.alexandria.java.fluent.IFluent_D;

public interface IOptional<T> extends IFluent_D<T>, IFluent_0<T>, IFluent1_<T> {
	@Override
	public IOptional<T> filter(Predicate<? super T> predicate);

	public <U> IOptional<U> flatMap(Function<? super T, ? extends IFluent1_<U>> mapper);

	@Override
	public <U> IOptional<U> map(Function<? super T, ? extends U> mapper);

	public IOptional<T> fallback(IOptional<? extends T> fallback);

	public IOptional<T> override(IOptional<? extends T> override);
}
