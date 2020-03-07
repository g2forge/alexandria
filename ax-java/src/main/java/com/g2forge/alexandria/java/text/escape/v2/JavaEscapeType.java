package com.g2forge.alexandria.java.text.escape.v2;

import com.g2forge.alexandria.java.core.marker.ISingleton;

import lombok.Getter;

public class JavaEscapeType implements IEscapeType, ISingleton {
	protected static final JavaEscapeType INSTANCE = new JavaEscapeType();

	public static JavaEscapeType create() {
		return INSTANCE;
	}

	@Getter
	protected final IEscaper escaper = new StandardEscaper("\\", null, "\b\n\r\f\"\\\t\'", "bnrf\"\\t'", 6);

	private JavaEscapeType() {}
}
