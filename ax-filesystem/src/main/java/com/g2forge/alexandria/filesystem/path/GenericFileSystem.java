package com.g2forge.alexandria.filesystem.path;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.g2forge.alexandria.java.concurrent.HConcurrent;
import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A generic implementation of the {@link FileSystem} class suitable for use in custom file system providers of all kinds. This implementation requires that you
 * implement {@link IGenericFileSystemProviderInternal} to provide it access to basic information.
 * 
 * Please note that at this time, this implementation does not yet support read only file systems or {@link #newWatchService() file watchers}.
 */
@RequiredArgsConstructor
public class GenericFileSystem extends FileSystem {
	@Getter(AccessLevel.PROTECTED)
	protected final IGenericFileSystemInternal<GenericPath> internal = new IGenericFileSystemInternal<GenericPath>() {
		@Override
		public FileSystem asFileSystem() {
			return GenericFileSystem.this;
		}

		@Override
		public IGenericFileSystemProviderInternal<GenericPath> getProvider() {
			return provider;
		}

		@Override
		public WatchKey register(GenericPath path, WatchService watcher, Kind<?>[] events, Modifier... modifiers) throws IOException {
			throw new UnsupportedOperationException(/* TODO */);
		}
	};

	protected final IGenericFileSystemProviderInternal<GenericPath> provider;

	protected final Map<String, ?> env;

	protected final Object lock = null;

	protected volatile transient boolean open = true;

	@Getter(lazy = true)
	private final List<Path> rootDirectories = HCollection.asList(new GenericPath(internal, ""));

	protected String buildPath(String first, String... more) {
		final String path;
		if (more.length == 0) path = first;
		else {
			final StringBuilder builder = new StringBuilder();
			builder.append(first);
			for (String segment : more) {
				if (segment.length() > 0) {
					if (builder.length() > 0) builder.append(getSeparator());
					builder.append(segment);
				}
			}
			path = builder.toString();
		}
		return path;
	}

	@Override
	public void close() throws IOException {
		HConcurrent.sync(() -> {
			open = false;
			provider.close();
		}, lock);
	}

	@Override
	public Iterable<FileStore> getFileStores() {
		return null;
	}

	@Override
	public Path getPath(String first, String... more) {
		final String path = buildPath(first, more);
		final List<String> list = HCollection.asList(path.split(Pattern.quote(getSeparator()) + "+"));
		if (list.isEmpty()) return new GenericPath(internal, "");
		if (list.get(0).length() < 1) return new GenericPath(internal, path.isEmpty() ? null : "", list.subList(1, list.size()));
		return new GenericPath(internal, null, list);
	}

	@Override
	public PathMatcher getPathMatcher(String syntaxAndPattern) {
		final int colon = syntaxAndPattern.indexOf(':');
		if ((colon <= 0) || (colon == (syntaxAndPattern.length() - 1))) throw new IllegalArgumentException(String.format("Both syntax and pattern are required (\"%1$s\" does not match the form \"syntax:pattern\")", syntaxAndPattern));

		final String syntax = syntaxAndPattern.substring(0, colon);
		final String pattern = syntaxAndPattern.substring(colon + 1);
		switch (syntax) {
			case "glob":
				return new GlobPathMatcher(pattern, getSeparator());
			case "regex":
				final Pattern regex = Pattern.compile(pattern);
				return path -> regex.matcher(path.toString()).matches();
			default:
				throw new UnsupportedOperationException(String.format("Syntax \"%1$s\" is not valid!", syntax));
		}
	}

	@Override
	public String getSeparator() {
		return "/";
	}

	@Override
	public UserPrincipalLookupService getUserPrincipalLookupService() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isOpen() {
		return HConcurrent.sync(() -> open, lock);
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public WatchService newWatchService() throws IOException {
		throw new UnsupportedOperationException(/* TODO */);
	}

	@Override
	public FileSystemProvider provider() {
		return provider.asFileSystemProvider();
	}

	@Override
	public Set<String> supportedFileAttributeViews() {
		return provider.getSupportedFileAttributeViews();
	}
}
