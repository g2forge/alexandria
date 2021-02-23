package com.g2forge.alexandria.filesystem.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Stack;

import com.g2forge.alexandria.filesystem.path.IGenericFileSystemInternal;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.io.HPath;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This class implements entry traversal to resolve paths against concrete file or directory abstractions. It does this by using an instance of
 * {@link IGenericEntryAccessor} to access file system entries, and a <code>check</code> method to verify that paths are from the appropriate file system and to
 * convert them to the standard {@link Path} if necessary.
 * 
 * @param <E> The generic type of entries in the file system. Conceptually a parent type of <code>F</code> and <code>D</code>, though this is not required.
 * @param <F> The generic type of files in the file system.
 * @param <D> The generic type of directories in the file system.
 * @param <P> The generic type of paths in the file system.
 */
@Getter
@AllArgsConstructor
public class GenericEntryReference<E, F, D, P extends Path> {
	protected final IGenericEntryAccessor<E, F, D, P> accessor;

	protected final IFunction1<? super Path, ? extends P> check;

	/**
	 * The parent of {@link #entry}. This is helpful when the caller wishes to delete the entry after resolving it.
	 * 
	 * @return The parent of {@link #entry}
	 */
	protected final D parent;

	/**
	 * The entry furthest from the <code>base</code> which could be resolved.
	 * 
	 * @return The furthest resolved entry.
	 */
	protected final E entry;

	/**
	 * The path to {@link GenericEntryReference#entry}.
	 * 
	 * @return The path to {@link GenericEntryReference#entry}.
	 */
	protected final P resolved;

	/**
	 * Any unresolved components of the <code>path</code> beyond {@link #entry} or <code>null</code> if the path was fully resolved.
	 * 
	 * @return Remaining components.
	 */
	protected final P remaining;

	/**
	 * Resolve <code>path</code> relative to <code>base</code>.
	 * 
	 * @param accessor An accessor which is used to traverse the file system.
	 * @param check A function to check paths and access them.
	 * @param base The directory to start from.
	 * @param path The path to resolve.
	 */
	public GenericEntryReference(IGenericEntryAccessor<E, F, D, P> accessor, IFunction1<? super Path, ? extends P> check, final D base, final P path) {
		if (accessor == null) throw new NullPointerException();
		if (base == null) throw new NullPointerException();
		this.accessor = accessor;
		this.check = check;

		final boolean isEmpty = HPath.isEmpty(path);

		final IGenericFileSystemInternal<P> internal = accessor.getGenericFileSystemInternal(path);
		final String self = internal.getSelf();
		final String parent = internal.getParent();

		final Stack<D> prev = new Stack<>();
		E current = accessor.asEntry(base);
		int i = 0;
		if (!isEmpty) {
			for (; i < path.getNameCount(); i++) {
				if ((current == null) || !accessor.isDirectory(current)) {
					current = null;
					break;
				}

				final Path name = path.getName(i);
				final D directory = accessor.asDirectory(current);
				prev.push(directory);

				final String string = name.toString();
				if (!Objects.equals(string, self)) {
					if (Objects.equals(string, parent)) prev.pop();
					else {
						final E child = accessor.getEntry(directory, string);
						if (child == null) {
							current = null;
							break;
						}
						current = child;
					}
				}
			}
		} else {
			prev.push(accessor.asDirectory(current));
			i++;
		}

		this.parent = prev.isEmpty() ? null : prev.peek();
		this.entry = current;
		this.resolved = check.apply(path.getRoot().resolve(path.subpath(0, i)).normalize());
		this.remaining = (i == path.getNameCount()) ? null : check.apply(path.subpath(i, path.getNameCount()));
	}

	/**
	 * Get the resolved directory, after ensuring that the resolution was complete, and the found entry was a directory.
	 * 
	 * @return The directory resolved by this reference.
	 * @throws IOException If the resolution failed or the found entry was not a directory.
	 */
	public D asDirectory() throws IOException {
		assertExists();
		if (!accessor.isDirectory(getEntry())) throw new NotDirectoryException(String.format("\"%1$s\" is not a directory!", getResolved()));
		return accessor.asDirectory(getEntry());
	}

	/**
	 * Get the resolved file, after ensuring that the resolution was complete, and the found entry was a file.
	 * 
	 * @return The file resolved by this reference.
	 * @throws IOException If the resolution failed or the found entry was not a file.
	 */
	public F asFile() throws IOException {
		assertExists();
		if (!accessor.isFile(getEntry())) throw new FileNotFoundException(String.format("\"%1$s\" is not a file!", getResolved()));
		return accessor.asFile(getEntry());
	}

	/**
	 * Ensure that the resolution was complete.
	 * 
	 * @throws NoSuchFileException If the path was not fully resolved.
	 */
	public void assertExists() throws NoSuchFileException {
		if (!isFullyResolved()) throw new NoSuchFileException(String.format("\"%1$s\" does not exist!", getRemaining()));
	}

	/**
	 * Ensure that the resolution was not complete.
	 * 
	 * @throws FileAlreadyExistsException If the path was fully resolved.
	 */
	public void asssertNotExists() throws FileAlreadyExistsException {
		if (isFullyResolved()) throw new FileAlreadyExistsException(String.format("\"%1$s\" already exists!", getResolved()));
	}

	/**
	 * Finish resolution of the entry, generally after a missing entry has been created.
	 * 
	 * @return A reference which is (hopefully more) fully resolved.
	 */
	public GenericEntryReference<E, F, D, P> finish() {
		if (isFullyResolved()) return this;
		return new GenericEntryReference<>(accessor, check, getParent(), getRemaining());
	}

	/**
	 * Get the file name of the missing entry which this references refers to.
	 * 
	 * @return The name of the missing entry.
	 * @throws NoSuchFileException If there were any missing parent directories.
	 */
	public String getCreateName() throws IOException {
		final String name = getRemaining().getName(0).toString();
		if (getRemaining().getNameCount() != 1) throw new NoSuchFileException(String.format("Ancestor \"%1$s\" does not exist!", getResolved().resolve(name)));
		return name;
	}

	/**
	 * Test if this reference was fully resolved.
	 * 
	 * @return <code>true</code> if this reference was fully resolved.
	 */
	public boolean isFullyResolved() {
		return getEntry() != null && getRemaining() == null;
	}
}
