package com.g2forge.alexandria.filesystem;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.g2forge.alexandria.filesystem.path.FileSystemPathURI;
import com.g2forge.alexandria.filesystem.path.GenericFileSystem;
import com.g2forge.alexandria.filesystem.path.GenericPath;
import com.g2forge.alexandria.filesystem.path.IGenericFileSystemProviderInternal;
import com.g2forge.alexandria.java.io.RuntimeIOException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public abstract class AProxyFileSystemProvider<P extends Path> extends FileSystemProvider {
	@RequiredArgsConstructor
	@Getter
	protected class Internal implements IGenericFileSystemProviderInternal<GenericPath> {
		@Getter
		protected final Path root;

		@Override
		public FileSystemProvider asFileSystemProvider() {
			return AProxyFileSystemProvider.this;
		}

		@Override
		public void close() {}

		@Override
		public Set<String> getSupportedFileAttributeViews() {
			return getRoot().getFileSystem().supportedFileAttributeViews();
		}

		@Override
		public URI toURI(GenericPath path) throws URISyntaxException {
			return new ProxyURI(getScheme(), getRoot(), path).toURI();
		}
	}

	public static class ProxyURI extends FileSystemPathURI {
		public static String encode(Path root) {
			try {
				return URLEncoder.encode(root.toString().replace('\\', '/'), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeIOException(e);
			}
		}

		public ProxyURI(String scheme, Path root) {
			this(scheme, root, (String) null);
		}

		public ProxyURI(String scheme, Path root, GenericPath path) {
			this(scheme, root, (path == null) ? null : path.toString());
		}

		public ProxyURI(String scheme, Path root, String path) {
			super(scheme, encode(root), path);
		}

		public ProxyURI(String scheme, URI uri) {
			super(scheme, uri);
		}

		public Path getRoot() {
			try {
				return Paths.get(URLDecoder.decode(getFileSystem(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeIOException(e);
			}
		}
	}

	protected final Map<Path, GenericFileSystem> fileSystems = new HashMap<>();

	protected abstract P check(Path path);

	@Override
	public void checkAccess(Path path, AccessMode... modes) throws IOException {
		final Path resolved = resolve(path);
		resolved.getFileSystem().provider().checkAccess(resolved, modes);
	}

	@Override
	public void copy(Path source, Path target, CopyOption... options) throws IOException {
		// Note that this method will never be called for files from different providers, that's the magic of NIO!
		Files.copy(resolve(source), resolve(target), options);
	}

	@Override
	public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
		Files.createDirectory(resolve(dir), attrs);
	}

	protected AProxyFileSystemProvider<P>.Internal createInternal(final Path root) {
		return new Internal(root);
	}

	@Override
	public void delete(Path path) throws IOException {
		Files.delete(resolve(path));
	}

	@Override
	public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
		return Files.getFileAttributeView(resolve(path), type, options);
	}

	@Override
	public FileStore getFileStore(Path path) throws IOException {
		return Files.getFileStore(resolve(path));
	}

	@Override
	public FileSystem getFileSystem(URI uri) {
		final Path root = new ProxyURI(getScheme(), uri).getRoot();
		synchronized (fileSystems) {
			return fileSystems.get(root);
		}
	}

	protected abstract Internal getInternal(P path);

	@Override
	public Path getPath(URI uri) {
		final ProxyURI parsedURI = new ProxyURI(getScheme(), uri);
		final Path root = parsedURI.getRoot();
		final GenericFileSystem fileSystem;
		synchronized (fileSystems) {
			fileSystem = fileSystems.get(root);
		}
		if (fileSystem == null) throw new NullPointerException(String.format("%2$s file system \"%1$s\" does not exist!", root, getScheme().toUpperCase()));
		return fileSystem.getPath(parsedURI.getPath());
	}

	@Override
	public boolean isHidden(Path path) throws IOException {
		return Files.isHidden(resolve(path));
	}

	@Override
	public boolean isSameFile(Path path0, Path path1) throws IOException {
		final Path checked0 = resolve(path0);
		final Path checked1 = resolve(path1);
		return Files.isSameFile(checked0, checked1);
	}

	@Override
	public void move(Path source, Path target, CopyOption... options) throws IOException {
		// Note that this method will never be called for files from different providers, that's the magic of NIO!
		Files.move(resolve(source), resolve(target), options);
	}

	@Override
	public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
		return Files.newByteChannel(resolve(path), options, attrs);
	}

	@Override
	public DirectoryStream<Path> newDirectoryStream(Path dir, Filter<? super Path> filter) throws IOException {
		return Files.newDirectoryStream(resolve(dir), filter);
	}

	@Override
	public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
		final ProxyURI parsedURI = new ProxyURI(getScheme(), uri);
		if ((parsedURI.getPath() != null) && !parsedURI.getPath().isEmpty()) throw new IllegalArgumentException("Cannot create a file system from a URI with a non-empty path!");
		final Path root = parsedURI.getRoot();
		synchronized (fileSystems) {
			if (fileSystems.containsKey(root)) throw new FileSystemAlreadyExistsException();
			final GenericFileSystem retVal = new GenericFileSystem(createInternal(root), env);
			fileSystems.put(root, retVal);
			return retVal;
		}
	}

	@Override
	public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
		return Files.readAttributes(resolve(path), type, options);
	}

	@Override
	public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
		return Files.readAttributes(resolve(path), attributes, options);
	}

	protected Path resolve(Path path) {
		final P checked = check(path);
		final Path root = getInternal(checked).getRoot();
		final Path relative = checked.getRoot().relativize(checked);
		final Path retVal = root.resolve(relative.toString());
		return retVal;
	}

	@Override
	public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
		Files.setAttribute(resolve(path), attribute, value, options);
	}
}
