package com.g2forge.alexandria.path.file.system;

import java.util.ArrayList;
import java.util.List;

import com.g2forge.alexandria.path.path.IPath;
import com.g2forge.alexandria.path.path.Path;

public interface IStandardFileSystem extends IFileSystem<String> {
	public default String getParent() {
		return "..";
	}

	public default String getSelf() {
		return ".";
	}

	@Override
	public default IPath<String> normalize(IPath<String> path) {
		if (path.isEmpty()) return path;

		final List<String> retVal = new ArrayList<>(path.getComponents().toCollection());
		for (int i = 0; i < retVal.size(); i++) {
			final String current = retVal.get(i);
			if (getSelf().equals(current)) retVal.remove(i--);
			else if (getParent().equals(current) && (i > 0) && !getParent().equals(retVal.get(i - 1))) {
				retVal.remove(i--);
				retVal.remove(i--);
			}
		}
		return new Path<>(retVal);
	}
}
