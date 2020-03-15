package com.g2forge.alexandria.java.text.escape;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class EscapeType implements IEscapeType {
	protected final IEscaper escaper;
}
