package com.g2forge.alexandria.java.core.enums;

import java.util.LinkedHashMap;
import java.util.Map;

import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HEnum {
	public static <E extends Enum<E>> E valueOfInsensitive(Class<E> klass, String text) {
		final Map<String, E> map = new LinkedHashMap<>();
		for (E value : klass.getEnumConstants()) {
			if (map.put(value.toString().toLowerCase(), value) != null) throw new IllegalArgumentException("Cannot perform case insensitive matching on enum \"" + klass + "\" because the members are not unique without casing!");
		}
		return map.get(text.toLowerCase());
	}

	public static <E extends Enum<E>> Class<E> getEnumClass(E object) {
		final Class<?> klass = object.getClass();
		final Class<?> declaringClass = klass.getDeclaringClass();
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final Class<E> enumClass = (Class) (declaringClass == null ? klass : declaringClass);
		return enumClass;
	}
}
