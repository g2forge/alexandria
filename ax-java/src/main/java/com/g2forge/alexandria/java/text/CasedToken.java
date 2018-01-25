package com.g2forge.alexandria.java.text;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CasedToken {
	public enum Type {
		Unknown,
		Word,
		Acronym;
	}

	protected final Type type;

	protected final String string;
}
