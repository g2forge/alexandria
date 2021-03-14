package com.g2forge.alexandria.command.invocation.environment;

import java.util.Collections;
import java.util.Map;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class MapEnvironment implements IEnvironment {
	@Singular
	protected final Map<String, String> variables;

	@Override
	public String apply(String name) {
		return getVariables().get(name);
	}

	@Override
	public Map<String, String> toMap() {
		return Collections.unmodifiableMap(getVariables());
	}
}
