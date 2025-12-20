package com.g2forge.alexandria.java.fluent.optional;

import java.util.function.Function;
import java.util.function.Predicate;

import com.g2forge.alexandria.java.fluent.IFluent10;
import com.g2forge.alexandria.java.fluent.IFluent1_;
import com.g2forge.alexandria.java.fluent.IFluent_D;

/**
 * Store zero or one values of the specified type. This interface is similar to {@link java.util.Optional}, but there are multiple implementations. See
 * {@link NullableOptional} and {@link NonNullOptional}.
 * 
 * @param <T> The type of the value, if any, held.
 */
public interface IOptional<T> extends IFluent_D<T>, IFluent10<T> {
	@Override
	public IOptional<T> fallback(IFluent10<? extends T> fallback);

	@Override
	public IOptional<T> filter(Predicate<? super T> predicate);

	@Override
	public <U> IOptional<U> flatMap(Function<? super T, ? extends IFluent1_<U>> mapper);

	@Override
	public <U> IOptional<U> map(Function<? super T, ? extends U> mapper);

	@Override
	public IOptional<T> override(IFluent10<? extends T> override);
}
