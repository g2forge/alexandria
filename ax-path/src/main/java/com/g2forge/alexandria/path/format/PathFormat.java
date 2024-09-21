package com.g2forge.alexandria.path.format;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PathFormat implements IStandardPathFormat<String> {
	Microsoft("\\"),
	POSIX("/");

	protected final String separator;

	@Override
	public String toString(String component) {
		return component;
	}
}
