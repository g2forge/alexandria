package com.g2forge.alexandria.java.io.file.compare;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.g2forge.alexandria.java.core.error.OrThrowable;

public abstract class AHashFileCompareGroupFunction implements IFileCompareGroupFunction<OrThrowable<String>> {
	protected HashFileCompareGroup computeGroup(final Path key, final OrThrowable<String> value) {
		return new HashFileCompareGroup(value);
	}

	protected abstract String computeHashString(Path path) throws IOException;

	@Override
	public Map<IFileCompareGroup, Set<Path>> group(Map<Path, ? extends OrThrowable<String>> hashes) {
		final Map<IFileCompareGroup, Set<Path>> retVal = new HashMap<>();
		for (Map.Entry<Path, ? extends OrThrowable<String>> entry : hashes.entrySet()) {
			final Path key = entry.getKey();
			final IFileCompareGroup group = computeGroup(key, entry.getValue());
			retVal.computeIfAbsent(group, k -> new LinkedHashSet<>()).add(key);
		}
		return retVal;
	}

	@Override
	public OrThrowable<String> hash(Path path) {
		try {
			return new OrThrowable<>(computeHashString(path));
		} catch (Throwable throwable) {
			return new OrThrowable<>(throwable);
		}
	}
}