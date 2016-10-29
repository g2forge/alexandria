package com.g2forge.alexandria.java.reflect;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum JavaPrimitive {
	BOOLEAN(Boolean.TYPE, Boolean.class, "Z"),
	BYTE(Byte.TYPE, Byte.class, "B"),
	CHAR(Character.TYPE, Character.class, "C"),
	DOUBLE(Double.TYPE, Double.class, "D"),
	FLOAT(Float.TYPE, Float.class, "F"),
	INT(Integer.TYPE, Integer.class, "I"),
	LONG(Long.TYPE, Long.class, "J"),
	SHORT(Short.TYPE, Short.class, "S"),
	VOID(Void.TYPE, Void.class, "V");

	public static JavaPrimitive of(Class<?> type, boolean primitive, boolean boxed) {
		for (JavaPrimitive element : JavaPrimitive.values()) {
			if (primitive && element.getPrimitive().equals(type)) return element;
			if (boxed && element.getBoxed().equals(type)) return element;
		}
		return null;
	}

	protected final Class<?> primitive;

	protected final Class<?> boxed;

	protected final String code;

	public String getUser() {
		return name().toLowerCase();
	}
}
