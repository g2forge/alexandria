package com.g2forge.alexandria.java.core.enums;

import java.util.LinkedHashMap;
import java.util.Map;

import com.g2forge.alexandria.java.core.marker.Helpers;
import com.g2forge.alexandria.java.function.IFunction1;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HEnum {
	public static <E extends Enum<E>> E valueOfInsensitive(Class<E> klass, String text) {
		return valueOf(klass, Enum::name, true, String::toLowerCase, text);
	}

	public static <E extends Enum<E>> E valueOf(Class<E> klass, IFunction1<? super E, ? extends String> toString, boolean ignoreNull, IFunction1<? super String, ? extends String> mangle, String text) {
		final Map<String, E> map = new LinkedHashMap<>();
		for (E value : klass.getEnumConstants()) {
			final String mangled = mangle.apply(toString.apply(value));
			if (ignoreNull && (mangled == null)) continue;
			if (map.put(mangled, value) != null) throw new IllegalArgumentException(String.format("Cannot perform matching on enum \"%1$s\" because the members are not unique under %2$s!", klass, mangle));
		}
		final String mangled = mangle.apply(text);
		if (!map.containsKey(mangled)) throw new IllegalArgumentException(String.format("Cannot find \"%1$s\" (mangled from \"%3$s\") in %2$s!", mangled, map.keySet(), text));
		return map.get(mangled);
	}

	public static <E extends Enum<E>> Class<E> getEnumClass(E object) {
		final Class<?> klass = object.getClass();
		final Class<?> declaringClass = klass.getDeclaringClass();
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final Class<E> enumClass = (Class) (declaringClass == null ? klass : declaringClass);
		return enumClass;
	}
}
