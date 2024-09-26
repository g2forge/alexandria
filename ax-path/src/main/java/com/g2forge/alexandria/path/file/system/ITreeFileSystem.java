package com.g2forge.alexandria.path.file.system;

import java.util.ArrayList;
import java.util.List;

import com.g2forge.alexandria.path.path.IPath;
import com.g2forge.alexandria.path.path.Path;

public interface ITreeFileSystem<T> extends IFileSystem<T> {
	public T getParent();

	@Override
	public default boolean isRootEscape(IPath<T> path) {
		return normalize(path).startsWith(new Path<>(getParent()));
	}

	@Override
	public default IPath<T> normalize(IPath<T> path) {
		if (path.isEmpty()) return path;

		final List<T> retVal = new ArrayList<>(path.getComponents().toCollection());
		for (int i = 0; i < retVal.size(); i++) {
			final T current = retVal.get(i);
			if (getParent().equals(current) && (i > 0) && !getParent().equals(retVal.get(i - 1))) {
				retVal.remove(i--);
				retVal.remove(i--);
			}
		}
		return new Path<>(retVal);
	}
}
