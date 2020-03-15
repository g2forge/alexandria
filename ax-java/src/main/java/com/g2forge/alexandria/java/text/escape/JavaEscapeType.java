package com.g2forge.alexandria.java.text.escape;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JavaEscapeType implements IEscapeType {
	String(new StandardEscaper("\\", null, "\b\n\r\f\"\\\t\'", "bnrf\"\\t'", 6)),
	PatternCharClass(new StandardEscaper("\\", null, "^[]&-\\", null, -1));

	protected final IEscaper escaper;
}
