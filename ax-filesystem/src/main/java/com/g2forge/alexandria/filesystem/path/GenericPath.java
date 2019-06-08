package com.g2forge.alexandria.filesystem.path;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.ProviderMismatchException;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import com.g2forge.alexandria.adt.range.IntegerRange;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.helpers.HObject;
import com.g2forge.alexandria.java.function.IPredicate1;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A generic implementation of the {@link Path} interface suitable for use in custom file system providers of all kinds. This implementation supports multiple
 * roots, and merely requires that you implement {@link IGenericFileSystemInternal} to provide it access to basic information.
 * 
 * Internally it uses a collection of strings which is not the most efficient method of path manipulation, but it is easy to extend and work with.
 */
@RequiredArgsConstructor
public class GenericPath implements Path {
	@Getter
	protected final IGenericFileSystemInternal<GenericPath> genericFileSystem;

	protected final String root;

	protected final List<String> names;

	public GenericPath(IGenericFileSystemInternal<GenericPath> fileSystem, String root, String... names) {
		this(fileSystem, root, HCollection.asList(names));
	}

	/**
	 * Ensure that this path is from an appropriate file system provider, without returning said file system provider.
	 * 
	 * @param providerPredicate A predicate to test this paths filesystem provider. If this predicate returns <code>false</code> this method will throw a
	 *            {@link ProviderMismatchException}.
	 * @return This path.
	 */
	public GenericPath check(IPredicate1<? super IGenericFileSystemProviderInternal<?>> providerPredicate) {
		if (!providerPredicate.test(getGenericFileSystem().getProvider())) throw new ProviderMismatchException(String.format("Path \"%1$s\" was not from the same filesystem provider!", this));
		return this;
	}

	protected GenericPath check(Path path) {
		if (path == null) throw new NullPointerException();
		if (!(path instanceof GenericPath)) throw new ProviderMismatchException(String.format("Path \"%1$s\" was not from the same filesystem provider!", path));
		final GenericPath retVal = (GenericPath) path;
		if (getGenericFileSystem() != retVal.getGenericFileSystem()) throw new ProviderMismatchException(String.format("Path \"%1$s\" was not from the same filesystem!", path));
		return retVal;
	}

	@Override
	public int compareTo(Path other) {
		final GenericPath otherChecked = check(other);
		final int root = this.root.compareTo(otherChecked.root);
		if (root != 0) return root;
		for (int i = 0, size = Math.min(names.size(), otherChecked.names.size()); i < size; i++) {
			final int name = names.get(i).compareTo(otherChecked.names.get(i));
			if (name != 0) return name;
		}
		return names.size() - otherChecked.names.size();
	}

	@Override
	public boolean endsWith(Path other) {
		final GenericPath otherChecked = check(other);
		if (names.size() < otherChecked.names.size()) return false;
		if (otherChecked.isAbsolute()) {
			if (!isAbsolute() || (names.size() != otherChecked.names.size())) return false;
			if (!Objects.equals(root, otherChecked.root)) return false;
		}
		return names.subList(names.size() - otherChecked.names.size(), names.size()).equals(otherChecked.names);
	}

	@Override
	public boolean endsWith(String other) {
		return endsWith(getFileSystem().getPath(other));
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
		if (!(o instanceof GenericPath)) return false;

		final GenericPath that = (GenericPath) o;
		if (getGenericFileSystem() != that.getGenericFileSystem()) return false;
		if (!Objects.equals(root, that.root)) return false;
		if (!Objects.equals(names, that.names)) return false;
		return true;
	}

	@Override
	public Path getFileName() {
		return subpath(true, -2, -1);
	}

	@Override
	public FileSystem getFileSystem() {
		return getGenericFileSystem().asFileSystem();
	}

	@Override
	public Path getName(int index) {
		return subpath(true, index, index + 1);
	}

	@Override
	public int getNameCount() {
		return names.size();
	}

	@Override
	public Path getParent() {
		return subpath(false, 0, -2);
	}

	@Override
	public Path getRoot() {
		return new GenericPath(getGenericFileSystem(), root);
	}

	@Override
	public int hashCode() {
		int retVal = System.identityHashCode(getGenericFileSystem());
		if (root != null) retVal = retVal * HObject.HASHPRIME + root.hashCode();
		if (names != null) retVal = retVal * HObject.HASHPRIME + names.hashCode();
		return retVal;
	}

	@Override
	public boolean isAbsolute() {
		return this.root != null;
	}

	@Override
	public Iterator<Path> iterator() {
		return new Iterator<Path>() {
			protected int i = 0;

			@Override
			public boolean hasNext() {
				return i < getNameCount();
			}

			@Override
			public Path next() {
				if (!hasNext()) throw new NoSuchElementException();

				final Path retVal = getName(i);
				i++;
				return retVal;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public GenericPath normalize() {
		final String self = getGenericFileSystem().getSelf();
		final String parent = getGenericFileSystem().getParent();

		final List<String> retVal = new ArrayList<>(names.size());
		boolean special = false;
		for (int i = 0; i < names.size(); i++) {
			final String name = names.get(i);
			if ((self != null) && self.equals(name)) {
				special = true;
				continue;
			}
			if ((parent != null) && parent.equals(name)) {
				if (retVal.size() > 0) {
					special = true;
					retVal.remove(retVal.size() - 1);
					continue;
				}
			}
			retVal.add(name);
		}
		if (!special) return this;
		return new GenericPath(getGenericFileSystem(), root, retVal);
	}

	@Override
	public WatchKey register(WatchService watcher, WatchEvent.Kind<?>... events) throws IOException {
		return register(watcher, events, new WatchEvent.Modifier[0]);
	}

	@Override
	public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException {
		return getGenericFileSystem().register(this, watcher, events, modifiers);
	}

	@Override
	public GenericPath relativize(Path other) {
		final GenericPath otherChecked = check(other);
		if (equals(otherChecked)) return new GenericPath(getGenericFileSystem(), null, getGenericFileSystem().getSelf());
		if (!Objects.equals(root, otherChecked.root)) throw new IllegalArgumentException();

		final int common;
		{
			int _common = 0;
			final int minSize = Math.min(this.getNameCount(), otherChecked.getNameCount());
			while ((_common < minSize) && Objects.equals(names.get(_common), otherChecked.names.get(_common)))
				_common++;
			common = _common;
		}
		final int parents = getNameCount() - common, fromOther = Math.max(otherChecked.getNameCount() - common, 0);
		final List<String> retVal = new ArrayList<>(parents + fromOther);
		for (int i = 0; i < parents; i++) {
			retVal.add(getGenericFileSystem().getParent());
		}
		if (fromOther > 0) retVal.addAll(otherChecked.names.subList(common, common + fromOther));
		return new GenericPath(getGenericFileSystem(), null, retVal);
	}

	@Override
	public GenericPath resolve(Path other) {
		final GenericPath otherChecked = check(other);
		if (otherChecked.isAbsolute()) return otherChecked;
		final List<String> names = HCollection.concatenate(this.names, otherChecked.names);
		return new GenericPath(getGenericFileSystem(), root, names);
	}

	@Override
	public Path resolve(String other) {
		return resolve(getFileSystem().getPath(other));
	}

	@Override
	public Path resolveSibling(Path other) {
		if (other == null) throw new NullPointerException("Other path cannot be null!");
		final Path parent = getParent();
		if (parent != null) return parent.resolve(other);
		final Path root = getRoot();
		if (root != null) return root.resolve(other);
		return other;
	}

	@Override
	public Path resolveSibling(String other) {
		return resolveSibling(getFileSystem().getPath(other));
	}

	@Override
	public boolean startsWith(Path other) {
		final GenericPath otherChecked = check(other);
		if ((isAbsolute() != otherChecked.isAbsolute()) || (names.size() < otherChecked.names.size())) return false;
		return Objects.equals(root, otherChecked.root) && names.subList(0, otherChecked.names.size()).equals(otherChecked.names);
	}

	@Override
	public boolean startsWith(String other) {
		return startsWith(getFileSystem().getPath(other));
	}

	protected GenericPath subpath(boolean forceRelative, int beginIndex, int endIndex) {
		final IntegerRange subrange = new IntegerRange(0, getNameCount()).wrapSubRange(new IntegerRange(beginIndex, endIndex));
		final String root = (forceRelative || (subrange.getMin() != 0)) ? null : this.root;
		final List<String> names = this.names.subList(subrange.getMin(), subrange.getMax());
		if ((this.root == root) && this.names.equals(names)) return this;
		if ((root == null) && names.isEmpty()) return new GenericPath(getGenericFileSystem(), root, getGenericFileSystem().getSelf());
		return new GenericPath(getGenericFileSystem(), root, names);
	}

	@Override
	public Path subpath(int beginIndex, int endIndex) {
		new IntegerRange(0, getNameCount()).validateSubRange(new IntegerRange(beginIndex, endIndex));
		return subpath(true, beginIndex, endIndex);
	}

	@Override
	public GenericPath toAbsolutePath() {
		if (isAbsolute()) return this;
		final Iterable<? extends Path> roots = getFileSystem().getRootDirectories();
		if (roots == null) throw new UnsupportedOperationException();
		final Iterator<? extends Path> iterator = roots.iterator();
		if (!iterator.hasNext()) throw new UnsupportedOperationException("File system has no root directories");
		final GenericPath root = check(iterator.next());
		if (iterator.hasNext()) throw new UnsupportedOperationException("File system has multiple root directories");
		return root.resolve(this);
	}

	@Override
	public File toFile() {
		if (getGenericFileSystem().asFileSystem() != FileSystems.getDefault()) throw new UnsupportedOperationException(String.format("Path \"%1$s\" does not come from the default file system and so cannot be converted to a file.  Please use NIO, or copy the file as needed!", this));
		return new File(toString());
	}

	@Override
	public Path toRealPath(LinkOption... options) throws IOException {
		final GenericPath retVal = normalize().toAbsolutePath();
		getFileSystem().provider().checkAccess(retVal);
		return retVal;
	}

	@Override
	public String toString() {
		final String retVal = names.stream().collect(Collectors.joining(getGenericFileSystem().asFileSystem().getSeparator()));
		if (root != null) return root + getFileSystem().getSeparator() + retVal;
		return retVal;
	}

	@Override
	public URI toUri() {
		try {
			return getGenericFileSystem().getProvider().toURI(this);
		} catch (URISyntaxException e) {
			throw new RuntimeException(String.format("Failed to convert path \"%1$s\" to a URI!", this), e);
		}
	}
}
