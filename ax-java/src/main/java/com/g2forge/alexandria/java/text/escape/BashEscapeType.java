package com.g2forge.alexandria.java.text.escape;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BashEscapeType implements IEscapeType {
	Single(new SingleCharacterEscaper("'\"", "\"'", "'", null, -1)),
	DoubleExpand(new SingleCharacterEscaper("\\", null, "\"\\$`", null, 2)),
	DoubleNoExpand(new SingleCharacterEscaper("\\", null, "\"\\$`", null, -1));

	protected final IEscaper escaper;
}
