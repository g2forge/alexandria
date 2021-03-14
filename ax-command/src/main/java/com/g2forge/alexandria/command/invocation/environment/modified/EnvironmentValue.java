package com.g2forge.alexandria.command.invocation.environment.modified;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Specify a string value for an environment variable.
 * 
 * @see EnvironmentModifier
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class EnvironmentValue implements IEnvironmentModifier {
	protected final String value;

	@Override
	public String modify(String value) {
		return getValue();
	}
}