package com.g2forge.alexandria.java.fluent.optional.function;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.helpers.HObject;
import com.g2forge.alexandria.java.fluent.optional.IOptional;
import com.g2forge.alexandria.java.fluent.optional.factory.IOptionalFactory;
import com.g2forge.alexandria.java.function.IPredicate2;
import com.g2forge.alexandria.java.function.LiteralSupplier;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@FunctionalInterface
public interface IOptionalFunction<I, O> extends Function<I, IOptional<? extends O>>, IOptionalFunctional<I, O, IOptionalFunction<? super I, ? extends O>> {
	@Data
	public static class Empty<I, O> implements IOptionalFunction<I, O> {
		protected final IOptionalFactory factory;

		@Override
		public IOptional<? extends O> apply(I i) {
			return factory.empty();
		}

		@Override
		public String toString() {
			return HObject.toString(this, "empty");
		}
	}

	@Data
	@RequiredArgsConstructor
	public static class Overridden<I, O> implements IOptionalFunction<I, O> {
		protected final List<IOptionalFunction<? super I, ? extends O>> functions;

		@SafeVarargs
		public Overridden(IOptionalFunction<? super I, ? extends O>... functions) {
			this(HCollection.asList(functions));
		}

		@Override
		public IOptional<? extends O> apply(I i) {
			IOptional<? extends O> o = null;
			for (IOptionalFunction<? super I, ? extends O> function : getFunctions()) {
				o = function.apply(i);
				if (!o.isEmpty()) return o;
			}
			return o;
		}

		@Override
		public IOptionalFunction<I, O> override(IOptionalFunction<? super I, ? extends O> override) {
			final List<IOptionalFunction<? super I, ? extends O>> functions = new ArrayList<>(getFunctions());
			functions.add(0, override);
			return new Overridden<I, O>(functions);
		}
	}

	@Data
	public static class Recursive<I, O> implements IOptionalFunction<I, O> {
		protected final BiPredicate<? super I, ? super I> terminate;

		protected final boolean prior;

		protected final Class<I> type;

		protected final IOptionalFunction<? super I, ? extends O> function;

		@Override
		public IOptional<? extends O> apply(I i) {
			I priorI = i;
			IOptional<? extends O> currentO = getFunction().apply(i);
			while (!currentO.isEmpty() && type.isInstance(currentO.get())) {
				final I currentI = type.cast(currentO.get());
				if (getTerminate().test(priorI, currentI)) return currentO;
				priorI = currentI;

				final IOptional<? extends O> optional = getFunction().apply(currentI);
				if (optional.isEmpty()) {
					if (prior) break;
					return optional;
				}
				currentO = optional;
			}
			return currentO;
		}
	}

	@Data
	@RequiredArgsConstructor
	public static class Restricted<I, O> implements IOptionalFunction<I, O> {
		protected final IOptionalFunction<? super I, ? extends O> function;

		protected final Predicate<? super I> predicate;

		protected final IOptional<O> empty;

		@Override
		public IOptional<? extends O> apply(I i) {
			if (!getPredicate().test(i)) return getEmpty();
			return getFunction().apply(i);
		}
	}

	public static <I, O> IOptionalFunction<I, O> empty(IOptionalFactory factory) {
		return new Empty<I, O>(factory);
	}

	public static <I, O> IOptionalFunction<I, O> of(IOptionalFactory factory, I input, O output) {
		return of(factory, input, new LiteralSupplier<>(output));
	}

	public static <I, O> IOptionalFunction<I, O> of(IOptionalFactory factory, I input, Supplier<? extends O> output) {
		return of(factory, null, input, output);
	}

	public static <I, O> IOptionalFunction<I, O> of(IOptionalFactory factory, IPredicate2<? super I, ? super I> equals, I input, O output) {
		return of(factory, equals, input, new LiteralSupplier<>(output));
	}

	public static <I, O> IOptionalFunction<I, O> of(IOptionalFactory factory, IPredicate2<? super I, ? super I> equals, I input, Supplier<? extends O> output) {
		return new IOptionalFunction<I, O>() {
			@Override
			public IOptional<? extends O> apply(I i) {
				return (equals == null ? input.equals(i) : equals.test(input, i)) ? factory.of(output.get()) : factory.empty();
			}

			@Override
			public String toString() {
				return HObject.toString(this, b -> {
					b.append(input).append('=').append(output);
					if (equals != null) b.append("(using ").append(equals).append(")");
				});
			}
		};
	}

	public static <I, O> IOptionalFunction<I, O> of(IOptionalFactory factory, Map<? super I, ? extends O> map) {
		return new IOptionalFunction<I, O>() {
			@Override
			public IOptional<? extends O> apply(I i) {
				return map.containsKey(i) ? factory.of(map.get(i)) : factory.empty();
			}

			@Override
			public String toString() {
				return HObject.toString(this, map);
			}
		};
	}

	@Override
	public default Function<I, O> fallback(Function<? super I, ? extends O> fallback) {
		return i -> {
			final IOptional<? extends O> o = apply(i);
			return o.isEmpty() ? fallback.apply(i) : o.get();
		};
	}

	@Override
	public default IOptionalFunction<I, O> override(IOptionalFunction<? super I, ? extends O> override) {
		return new Overridden<I, O>(override, this);
	}

	@Override
	public default IOptionalFunction<I, O> recursive(BiPredicate<? super I, ? super I> terminate, boolean prior, Class<I> type) {
		return new Recursive<I, O>(terminate, prior, type, this);
	}

	@Override
	public default IOptionalFunction<I, O> restrict(Predicate<? super I> predicate, IOptionalFactory factory) {
		return new Restricted<>(this, predicate, factory.empty());
	}
}
