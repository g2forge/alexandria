package com.g2forge.alexandria.service;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BasicServiceLoader<S> implements IServiceLoader<S> {
	@Getter
	protected final Class<S> type;

	public Stream<? extends S> load() {
		return StreamSupport.stream(java.util.ServiceLoader.load(getType()).spliterator(), false);
	}
}
