package com.g2forge.alexandria.command.invocation.environment.modified;

import java.util.Map;

import com.g2forge.alexandria.command.invocation.environment.IEnvironment;
import com.g2forge.alexandria.java.core.helpers.HMap;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class ModifiedEnvironment implements IEnvironment {
	protected final IEnvironment base;

	@Singular
	protected final Map<String, IEnvironmentModifier> modifiers;

	@Override
	public String apply(String name) {
		final IEnvironmentModifier modifier = getModifiers().getOrDefault(name, EnvironmentModifier.Inherit);
		final String base = getBase().apply(name);
		return modifier.modify(base);
	}

	public IEnvironment simplify() {
		if (getModifiers().isEmpty()) return getBase();
		return this;
	}

	@Override
	public Map<String, String> toMap() {
		final Map<String, String> retVal = HMap.copy(getBase().toMap());
		for (Map.Entry<String, IEnvironmentModifier> entry : getModifiers().entrySet()) {
			final String variable = entry.getKey();

			final String base = retVal.get(variable);
			final String modified = entry.getValue().modify(base);
			if (modified == null) retVal.remove(variable);
			else retVal.put(variable, modified);
		}

		return HMap.unmodifiableMap(retVal);
	}
}
