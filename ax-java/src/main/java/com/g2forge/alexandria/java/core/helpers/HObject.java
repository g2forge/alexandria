package com.g2forge.alexandria.java.core.helpers;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import com.g2forge.alexandria.java.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HObject {
	@SafeVarargs
	public static <T> boolean equals(boolean type, T _this, Object obj, Function<? super T, ?>... accessors) {
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

	public static final int HASHPRIME = 31;

	@SafeVarargs
	public static <T> int hashCode(T _this, Function<? super T, ?>... accessors) {
		int result = 1;
		for (Function<? super T, ?> accessor : accessors) {
			final Object value = accessor.apply(_this);
			result = HASHPRIME * result + ((value == null) ? 0 : value.hashCode());
		}
		return result;
	}

	@SafeVarargs
	public static <T> String toString(T _this, Function<? super T, ?>... accessors) {
		return toString(_this, b -> {
			boolean first = true;
			for (Function<? super T, ?> accessor : accessors) {
				if (first) first = false;
				else b.append(", ");
				b.append(accessor.apply(_this));
			}
		});
	}

	public static <T> String toString(T _this, Consumer<StringBuilder> consumer) {
		final StringBuilder retVal = new StringBuilder();
		retVal.append(_this.getClass().getSimpleName()).append('(');
		consumer.accept(retVal);
		retVal.append(')');
		return retVal.toString();
	}

	public static <T> String toString(T _this, Object value) {
		return toString(_this, b -> b.append(value));
	}
}
