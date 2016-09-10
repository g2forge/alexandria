package com.g2forge.alexandria.java.core.helpers;

import java.util.Objects;
import java.util.function.Function;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ObjectHelpers {
	@SafeVarargs
	public static final <T> boolean equals(boolean type, T _this, Object obj, Function<? super T, ?>... accessors) {
		if (_this == obj) return true;
		if (obj == null) return false;
		if (type && (_this.getClass() != obj.getClass())) return false;

		@SuppressWarnings("unchecked")
		final T that = (T) obj;
		for (Function<? super T, ?> accessor : accessors) {
			final Object left = accessor.apply(_this);
			final Object right = accessor.apply(that);
			if (!Objects.equals(left, right)) return false;
		}
		return true;
	}

	@SafeVarargs
	public static final <T> int hashCode(T _this, Function<? super T, ?>... accessors) {
		final int prime = 31;
		int result = 1;
		for (Function<? super T, ?> accessor : accessors) {
			final Object value = accessor.apply(_this);
			result = prime * result + ((value == null) ? 0 : value.hashCode());
		}
		return result;
	}
}
