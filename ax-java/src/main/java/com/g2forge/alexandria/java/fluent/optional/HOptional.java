package com.g2forge.alexandria.java.fluent.optional;

import java.util.Optional;

import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HOptional {
	public static <T> Optional<T> upcast(Optional<? extends T> optional) {
		return Optional.ofNullable(optional.orElseGet(null));
	}
}
