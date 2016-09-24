package com.g2forge.alexandria.java.fluent.optional.factory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.LiteralSupplier;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OptionalFunctionBuilder<I, O, R> {
	protected final IOptionalFunctionFactory<I, O, R> factory;

	protected final Map<Object, Supplier<? extends O>> map = new LinkedHashMap<>();

	public OptionalFunctionBuilder<I, O, R> add(I input, O output) {
		map.put(input, new LiteralSupplier<>(output));
		return this;
	}

	public OptionalFunctionBuilder<I, O, R> add(I input, Supplier<? extends O> output) {
		map.put(input, output);
		return this;
	}

	public OptionalFunctionBuilder<I, O, R> add(Map<? super I, ? extends O> map) {
		map.entrySet().forEach(e -> this.map.put(e.getKey(), new LiteralSupplier<>(e.getValue())));
		return this;
	}

	public R build() {
		if (map.isEmpty()) return factory.empty();
		else if (map.size() == 1) {
			final Map.Entry<Object, Supplier<? extends O>> entry = HCollection.getOne(map.entrySet());
			if (entry.getValue() instanceof LiteralSupplier) return factory.of(entry.getKey(), LiteralSupplier.unwrap(entry.getValue()));
			else return factory.of(entry.getKey(), entry.getValue());
		} else {
			if (map.values().stream().filter(s -> !(s instanceof LiteralSupplier)).findAny().isPresent()) return factory.wrap(i -> map.containsKey(i) ? factory.getOptionalFactory().of(map.get(i).get()) : factory.getOptionalFactory().empty());
			else return factory.of(map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get())));
		}
	}
}