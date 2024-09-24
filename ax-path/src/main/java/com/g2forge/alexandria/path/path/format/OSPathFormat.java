package com.g2forge.alexandria.path.path.format;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OSPathFormat implements IStandardPathFormat<String> {
	Microsoft("\\"),
	POSIX("/");

	protected final String separator;

	@Override
	public String toComponent(String component) {
		return component;
	}

	@Override
	public String toString(String component) {
		return component;
	}
}
