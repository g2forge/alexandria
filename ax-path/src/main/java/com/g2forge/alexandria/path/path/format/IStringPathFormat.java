package com.g2forge.alexandria.path.path.format;

import com.g2forge.alexandria.path.path.IPath;

public interface IStringPathFormat<P extends IPath<String>> extends IStandardPathFormat<String, P> {
	@Override
	public default String toComponent(String component) {
		return component;
	}

	@Override
	public default String toString(String component) {
		return component;
	}
}
