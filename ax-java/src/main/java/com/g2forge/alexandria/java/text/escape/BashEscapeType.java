package com.g2forge.alexandria.java.text.escape;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BashEscapeType implements IEscapeType {
	Single(new StandardEscaper("'\"", "\"'", "'", null, -1)),
	DoubleExpand(new StandardEscaper("\\", null, "\"\\$`", null, 2)),
	DoubleNoExpand(new StandardEscaper("\\", null, "\"\\$`", null, -1));

	protected final IEscaper escaper;
}
