package com.g2forge.alexandria.path.format;

import java.util.stream.Collectors;

import com.g2forge.alexandria.path.IPath;

public interface IStandardPathFormat<T> extends IPathFormat<T> {
	public String getSeparator();

	@Override
	public default String toString(IPath<T> path) {
		return path.getComponents().stream().map(this::toString).collect(Collectors.joining(getSeparator()));
	}
}
