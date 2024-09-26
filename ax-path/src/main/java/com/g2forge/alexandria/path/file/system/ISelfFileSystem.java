package com.g2forge.alexandria.path.file.system;

import java.util.ArrayList;
import java.util.List;

import com.g2forge.alexandria.path.path.IPath;
import com.g2forge.alexandria.path.path.Path;

public interface ISelfFileSystem<T> extends IFileSystem<T> {
	public T getSelf();

	@Override
	public default IPath<T> normalize(IPath<T> path) {
		if (path.isEmpty()) return path;

		final List<T> retVal = new ArrayList<>(path.getComponents().toCollection());
		for (int i = 0; i < retVal.size(); i++) {
			final T current = retVal.get(i);
			if (getSelf().equals(current)) retVal.remove(i--);
		}
		return new Path<>(retVal);
	}
}
