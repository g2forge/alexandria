package com.g2forge.alexandria.match;

import java.util.Objects;

import com.g2forge.alexandria.java.function.IPredicate1;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class LiteralMatch implements IPredicate1<String> {
	protected final String value;

	@Override
	public boolean test(String string) {
		return Objects.equals(string, getValue());
	}
}
