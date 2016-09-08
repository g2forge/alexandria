package com.g2forge.alexandria.java.optional;

import java.util.Optional;

public class OptionalHelpers {
	public static <T> Optional<T> upcast(Optional<? extends T> optional) {
		return Optional.ofNullable(optional.orElseGet(null));
	}
}
