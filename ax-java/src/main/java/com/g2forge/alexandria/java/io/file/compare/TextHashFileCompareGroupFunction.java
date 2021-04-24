package com.g2forge.alexandria.java.io.file.compare;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.g2forge.alexandria.java.core.error.HError;
import com.g2forge.alexandria.java.core.error.OrThrowable;
import com.g2forge.alexandria.java.core.helpers.HBinary;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.java.io.HIO;
import com.g2forge.alexandria.java.io.HTextIO;

public class TextHashFileCompareGroupFunction extends AHashFileCompareGroupFunction implements ISingleton {
	protected static final TextHashFileCompareGroupFunction INSTANCE = new TextHashFileCompareGroupFunction();

	public static TextHashFileCompareGroupFunction create() {
		return INSTANCE;
	}

	@Override
	protected HashFileCompareGroup computeGroup(final Path key, final OrThrowable<String> value) {
		return new HashFileCompareGroup(value) {
			@Override
			public String describe(Collection<Path> roots, Path relative) {
				final StringBuilder retVal = new StringBuilder();
				retVal.append(super.describe(roots, relative));

				if (!value.isEmpty()) {
					retVal.append(": ");
					try (final InputStream stream = Files.newInputStream(HCollection.getAny(roots).resolve(relative))) {
						retVal.append(HTextIO.readAll(stream, true));
					} catch (IOException e) {
						retVal.append(HError.toString(e));
					}
				}

				return retVal.toString();
			}
		};
	}

	@Override
	protected String computeHashString(Path path) throws IOException {
		try (final InputStream stream = Files.newInputStream(path)) {
			return HBinary.toHex(HIO.sha1(HTextIO.readAll(stream, true)));
		}
	}

	@Override
	public Map<IFileCompareGroup, Set<Path>> group(Map<Path, ? extends OrThrowable<String>> hashes) {
		final Map<IFileCompareGroup, Set<Path>> retVal = new HashMap<>();
		for (Map.Entry<Path, ? extends OrThrowable<String>> entry : hashes.entrySet()) {
			final IFileCompareGroup group = computeGroup(entry.getKey(), entry.getValue());
			retVal.computeIfAbsent(group, k -> new LinkedHashSet<>()).add(entry.getKey());
		}
		return retVal;
	}
}