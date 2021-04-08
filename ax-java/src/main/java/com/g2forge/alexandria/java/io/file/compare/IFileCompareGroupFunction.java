package com.g2forge.alexandria.java.io.file.compare;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public interface IFileCompareGroupFunction<T> {
	public Map<IFileCompareGroup, Set<Path>> group(Map<Path, ? extends T> hashes);

	public T hash(Path path);
}