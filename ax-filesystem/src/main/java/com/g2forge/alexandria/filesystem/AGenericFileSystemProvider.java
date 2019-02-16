package com.g2forge.alexandria.filesystem;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.g2forge.alexandria.filesystem.attributes.FileAttributeName;
import com.g2forge.alexandria.filesystem.attributes.IGenericBasicAttributeModifier;
import com.g2forge.alexandria.filesystem.attributes.accessor.AttributeViewAccessorRegistry;
import com.g2forge.alexandria.filesystem.attributes.accessor.IAttributeViewAccessor;
import com.g2forge.alexandria.filesystem.path.IGenericFileSystemProviderInternal;
import com.g2forge.alexandria.filesystem.sync.ISyncFactory;
import com.g2forge.alexandria.filesystem.sync.SyncFactory;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.helpers.HCollector;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.IFunction2;
import com.g2forge.alexandria.java.function.IThrowConsumer1;
import com.g2forge.alexandria.java.function.IThrowFunction1;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * A generic partial implementation of {@link FileSystemProvider} designed to make it easier for an implementor to support the proper APIs.
 * 
 * In the context of this class we will refer to an "entry" which is the hypothetical parent type of a "file" or "directory". "Entry" is short for "directory
 * entry" and refers to a object in the file system, whose type we do not know at the time of writing this documentation. Your implementation may or may not
 * have a concrete type corresponding to this.
 * 
 * This implementation will handle things like locking and basic file attributes (e.g. file times), while the implementor must implement actual operations. This
 * class does not handle permissions, for example, as different file systems have different permissions. At this time it may or may not appropriately support
 * symbolic links, permissions, file stores and multiple root directories. It does not support watchers yet. Please feel free to open a pull request when
 * appropriate. See {@link com.g2forge.alexandria.filesystem.ATestFileSystemProvider} to test any file system provider, not just those which extend this
 * abstract class.
 * 
 * An implementation can override the {@link #syncFactory sync factory} to do things like call
 * {@link com.g2forge.alexandria.java.function.IFunctional#wrap(com.g2forge.alexandria.java.function.IRunnable, com.g2forge.alexandria.java.function.IRunnable)},
 * in order to receive callbacks when file operations start and end. This can be used, for example, to initiate a remote connection and push results to the
 * remote system.
 * 
 * @param <P> The type of paths used by this file system provider.
 * @param <Internal> The type of the internal. Please note that a separate internal may be created per-filesystem if you wish to add state will still using
 *            {@link com.g2forge.alexandria.filesystem.path.GenericFileSystem}. These must be {@link Comparable} in order for
 *            {@link #wrapN(IThrowConsumer1, IFunction2, Path...)} to ensure locking order.
 * @param <R> A reference to an entry in this file system. This is a purely internal type.
 */
@RequiredArgsConstructor
public abstract class AGenericFileSystemProvider<P extends Path, Internal extends IGenericFileSystemProviderInternal<P>, R> extends FileSystemProvider {
	/**
	 * A temporary type for the necessary information to be returned by {@link AGenericFileSystemProvider#copy(Object, CopyOption...)} and
	 * {@link AGenericFileSystemProvider#move(Object, CopyOption...)}.
	 * 
	 * During a copy, the attribute modifiers are for the source and target entries. During a move, the attribute modifiers will be for the parent of the source
	 * and target entries.
	 * 
	 * @param <R> See {@link AGenericFileSystemProvider} for a description of the <code>R</code> type parameter.
	 */
	@Data
	@Builder
	@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
	protected static class CopyResult<R> {
		/**
		 * A modifier for the source of the copy or the parent directory of the target of the move.
		 */
		protected final IGenericBasicAttributeModifier sourceAttributeModifier;

		/**
		 * A modifier for the target of the copy or the parent directory of the target of the move.
		 */
		protected final IGenericBasicAttributeModifier targetAttributeModifier;

		/**
		 * A function to be called to complete the copy. The input will be a reference to parent of the copy target. The output will be used to modify the
		 * attributes of the parent of the copy target.
		 */
		protected final IThrowFunction1<? super R, ? extends IGenericBasicAttributeModifier, IOException> completion;
	}

	/**
	 * A temporary type for the necessary information to be returned by {@link AGenericFileSystemProvider#openFile(Object, Set)} and
	 * {@link AGenericFileSystemProvider#createDirectory(Object)}.
	 * 
	 * @param <R> See {@link AGenericFileSystemProvider} for a description of the <code>R</code> type parameter.
	 */
	@Data
	@Builder
	@AllArgsConstructor(access = AccessLevel.PUBLIC)
	public static class OpenResult<R> {
		/**
		 * A modifier for the parent of the entry being opened.
		 */
		protected final IGenericBasicAttributeModifier parentAttributeModifier;

		/**
		 * A modifier for the attributes of the file or directory. Must be <code>null</code> if the entry already existed.
		 */
		protected final IGenericBasicAttributeModifier entryAttributeModifier;

		/**
		 * A reference to the file. Must be <code>null</code> if the file already existed.
		 */
		protected final R created;
	}

	/**
	 * Check that the specified path is from this file system provider, and convert it to whatever concrete type we use.
	 * 
	 * @param path The path to check and convert.
	 * @return An internal representation of the specified path, possibly a cast or a tear-off interface which has special access to internal structures.
	 */
	protected abstract P check(Path path);

	@Override
	public void checkAccess(Path path, AccessMode... modes) throws IOException {
		final P checked = check(path);
		this.<IThrowConsumer1<P, IOException>, IOException>wrap1(p -> {
			// Delegate to the implementation
			checkAccess(resolve(p), modes);
		}, getSyncFactory().getSyncThrowConsumer1(), checked).accept(checked);
	}

	/**
	 * Check that the specified access modes are supported for the referenced entry.
	 * 
	 * @param reference A reference to the entry.
	 * @param modes An array of access modes to be checked.
	 * @throws IOException If one or more of the access modes is not allowed.
	 */
	protected abstract void checkAccess(R reference, AccessMode... modes) throws IOException;

	@Override
	public void copy(Path source, Path target, CopyOption... options) throws IOException {
		if (isSameFile(source, target)) return;
		final P checkedSource = check(source);
		final P checkedTarget = check(target);

		final IThrowFunction1<? super R, ? extends IGenericBasicAttributeModifier, IOException> completion = this.<IThrowFunction1<P, IThrowFunction1<? super R, ? extends IGenericBasicAttributeModifier, IOException>, IOException>, IOException>wrap1(p -> {
			final R refSource = resolve(p);

			// Delegate to the implementation
			final CopyResult<R> result = copy(refSource, options);

			// Update the time stamps
			result.getSourceAttributeModifier().access(null);
			result.getTargetAttributeModifier().access(result.getSourceAttributeModifier().getAccessTime());

			return result.getCompletion();
		}, getSyncFactory().getSyncThrowFunction1(), checkedSource).apply(checkedSource);

		this.<IThrowConsumer1<P, IOException>, IOException>wrap1(p -> {
			final R refTarget = resolve(p);
			// Delegate to the implementation using the completion callback
			completion.apply(refTarget).modify(null);
		}, getSyncFactory().getSyncThrowConsumer1(), checkedTarget).accept(checkedTarget);
	}

	/**
	 * Perform the source half of the copy operation, and return the {@link CopyResult#completion} for the target half. This method will never be called for
	 * files which are the same. This method must handle copying attributes if requested by {@link StandardCopyOption#COPY_ATTRIBUTES}. This method should, when
	 * copying a directory, create an empty directory with only any special files (such as "." and "..").
	 * 
	 * The {@link CopyResult#completion} must handle the {@link StandardCopyOption#REPLACE_EXISTING} option. If that option is not specified, and the target
	 * exists, the completion must throw {@link FileAlreadyExistsException}.
	 * 
	 * @param refSource A reference to the entry to be copied.
	 * @param options Copy options.
	 * @return An instance of {@link CopyResult} which includes the completion for the target half of the copy.
	 * @throws IOException
	 */
	protected abstract CopyResult<R> copy(R refSource, CopyOption... options) throws IOException;

	/**
	 * Create a channel for accessing the regular file specified by the reference. The reference will always be to an already existing file.
	 * 
	 * @param read Should the resulting channel support read operations?
	 * @param write Should the resulting channel support write operations?
	 * @param reference A reference to a regular file.
	 * @return A channel which can be used to read and/or write the specified file.
	 * @throws IOException
	 */
	protected abstract SeekableByteChannel createChannel(final boolean read, final boolean write, final R reference) throws IOException;

	@Override
	public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
		final P checked = check(dir);
		this.<IThrowConsumer1<P, FileSystemException>, FileSystemException>wrap1(p -> {
			final R reference = resolve(p);
			// Delegate to the implementation
			final OpenResult<R> result = createDirectory(reference);
			result.getParentAttributeModifier().modify(null);
			setAttributes(result.getCreated(), attrs);
		}, getSyncFactory().getSyncThrowConsumer1(), checked).accept(checked);
	}

	/**
	 * Create a directory. This method must ensure that the directory does not already exist, and that the parent does. Because of this the
	 * {@link OpenResult#created} field must never be <code>null</code> in the return value.
	 * 
	 * @param reference A reference to the directory to create.
	 * @return
	 * @throws FileAlreadyExistsException If the reference is to a pre-existing entry.
	 * @throws NoSuchFileException If the parent directory does not exist.
	 */
	protected abstract OpenResult<R> createDirectory(final R reference) throws FileAlreadyExistsException, NoSuchFileException;

	@Override
	public void delete(Path path) throws IOException {
		final P checked = check(path);
		this.<IThrowConsumer1<P, IOException>, IOException>wrap1(p -> {
			final R reference = resolve(p);
			// Delegate to the implementation and update the timestamps
			delete(reference).modify(null);
		}, getSyncFactory().getSyncThrowConsumer1(), checked).accept(checked);
	}

	/**
	 * Delete the specified entry. This method must fail if the referenced entry does not exist, or if the entry is a non-empty directory.
	 * 
	 * @param reference A reference to the file to delete
	 * @return A basic attribute modifier for the parent directory of the file to delete
	 * @throws NoSuchFileException
	 * @throws DirectoryNotEmptyException
	 */
	protected abstract IGenericBasicAttributeModifier delete(final R reference) throws NoSuchFileException, DirectoryNotEmptyException;

	/**
	 * Get a registry of all the attribute view accessors supported by this file system provider. This will generally return a singleton registry which has been
	 * initialized with all the relevant factories. In order to implement proper attribute support you will need to implement at least a single
	 * {@link IAttributeViewAccessor} for {@link BasicFileAttributes basic attributes}.
	 * 
	 * @return A registry of all the attribute view accessors supported by this file system provider.
	 */
	protected abstract AttributeViewAccessorRegistry<? super R> getAVARegistry();

	@Override
	public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
		final IFunction1<? super R, ? extends IAttributeViewAccessor<?, V>> factory = getAVARegistry().getByView(type);
		final P checked = check(path);
		return this.<IFunction1<P, V>, RuntimeException>wrap1(p -> {
			final R reference = resolve(p);
			return factory.apply(reference).getView();
		}, getSyncFactory().getSyncFunction1(), checked).apply(checked);
	}

	/**
	 * Get the internal file system for the specified path.
	 * 
	 * @param path The path to get the filesystem for.
	 * @return An internal object, used for locking and perhaps for internal state of a specific file system.
	 */
	protected abstract Internal getInternal(final P path);

	/**
	 * Get the ordered list of locks that should be acquired before an operation over the specified paths. The order of the locks is the order in which they
	 * will be acquired. This method can therefor implement two-phase locking.
	 * 
	 * @param paths The paths that will be accessed in the operaton.
	 * @return The list of locks to be held. These will be converted to a nested series of synchronizations.
	 */
	protected abstract List<Object> getLocks(@SuppressWarnings("unchecked") P... paths);

	protected ISyncFactory getSyncFactory() {
		return SyncFactory.create();
	}

	@Override
	public void move(Path source, Path target, CopyOption... options) throws IOException {
		if (isSameFile(source, target)) return;
		if (HCollection.asSet(options).contains(StandardCopyOption.ATOMIC_MOVE)) {
			@SuppressWarnings("unchecked")
			final P[] checked = (P[]) Stream.of(source, target).map(this::check).collect(HCollector.toArray(Path.class));
			this.<IThrowConsumer1<P[], IOException>, IOException>wrapN(p -> {
				final R refSource = resolve(p[0]);
				// Delegate to the implementation
				final CopyResult<R> result = move(refSource, options);
				final IGenericBasicAttributeModifier sourceParentAttributes = result.getSourceAttributeModifier();

				final R refTarget = resolve(p[1]);
				final IGenericBasicAttributeModifier targetParentAttributes = result.getCompletion().apply(refTarget);

				targetParentAttributes.modify(null);
				sourceParentAttributes.modify(targetParentAttributes.getModifyTime());
			}, getSyncFactory().getSyncThrowConsumerN(), checked).accept(checked);
		} else {
			final P checkedSource = check(source);
			final P checkedTarget = check(target);

			final IThrowFunction1<? super R, ? extends IGenericBasicAttributeModifier, IOException> completion = this.<IThrowFunction1<P, IThrowFunction1<? super R, ? extends IGenericBasicAttributeModifier, IOException>, IOException>, IOException>wrap1(p -> {
				final R refSource = resolve(p);
				// Delegate to the implementation
				final CopyResult<R> result = move(refSource, options);
				result.getSourceAttributeModifier().modify(null);
				return result.getCompletion();
			}, getSyncFactory().getSyncThrowFunction1(), checkedSource).apply(checkedSource);

			this.<IThrowConsumer1<P, IOException>, IOException>wrap1(p -> {
				final R refTarget = resolve(p);
				// Delegate to the implementation through the completion method
				completion.apply(refTarget).modify(null);
			}, getSyncFactory().getSyncThrowConsumer1(), checkedTarget).accept(checkedTarget);
		}
	}

	/**
	 * Perform the source half of the move operation, and return the {@link CopyResult#completion} for the target half. This method will never be called for
	 * files which are the same.
	 * 
	 * The {@link CopyResult#completion} must handle the {@link StandardCopyOption#REPLACE_EXISTING} option. If that option is not specified, and the target
	 * exists, the completion must throw {@link FileAlreadyExistsException}.
	 * 
	 * @param refSource A reference to the entry to be copied.
	 * @param options Copy (well... move) options.
	 * @return An instance of {@link CopyResult} which includes the completion for the target half of the copy.
	 * @throws IOException
	 */
	protected abstract CopyResult<R> move(R refSource, CopyOption... options) throws IOException;

	@Override
	public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
		final P checked = check(path);
		return this.<IThrowFunction1<P, SeekableByteChannel, IOException>, IOException>wrap1(p -> {
			final R reference = resolve(p);

			final OpenResult<R> result = openFile(reference, options);
			if (result.getCreated() != null) {
				// If we created the file, then update the parent modification time to match
				result.getParentAttributeModifier().modify(result.getEntryAttributeModifier().getCreateTime());
				// ... and set any user specified attributes
				setAttributes(result.getCreated(), attrs);
			} else {
				// Since we're not creating the file, just mark the parent accessed
				result.getParentAttributeModifier().access(null);
			}

			// Delegate to the implementation
			final boolean write = options.contains(StandardOpenOption.WRITE);
			final SeekableByteChannel retVal = createChannel(options.contains(StandardOpenOption.READ), write, result.getCreated() == null ? reference : result.getCreated());

			// Handle the truncate and append options
			if ((result.getCreated() == null) && write && options.contains(StandardOpenOption.TRUNCATE_EXISTING)) retVal.truncate(0);
			if (write && options.contains(StandardOpenOption.APPEND)) retVal.position(retVal.size());

			return retVal;
		}, getSyncFactory().getSyncThrowFunction1(), checked).apply(checked);
	}

	@Override
	public DirectoryStream<Path> newDirectoryStream(Path dir, Filter<? super Path> filter) throws IOException {
		final P checked = check(dir);
		return this.<IThrowFunction1<P, DirectoryStream<Path>, IOException>, IOException>wrap1(p -> {
			final R reference = resolve(p);
			// Delegate to the implementation
			return toDirectoryStream(reference, dir);
		}, getSyncFactory().getSyncThrowFunction1(), checked).apply(checked);
	}

	/**
	 * Open the specified file, by creating it or failing as appropriate. Note that this method is not responsible for creating a channel to access the file, as
	 * that is handled by {@link #createChannel(boolean, boolean, Object)}. It is responsible only for the open options {@link StandardOpenOption#CREATE} and
	 * {@link StandardOpenOption#CREATE_NEW}.
	 * 
	 * @param reference A reference to the file to open.
	 * @param options All of the open options, though this method need only handle a subset of them.
	 * @return An instance of {@link OpenResult} to allow {@link #newByteChannel(Path, Set, FileAttribute...)} to create the appropriate channel.
	 * @throws FileAlreadyExistsException If the file already exists and {@link StandardOpenOption#CREATE_NEW} has been specified.
	 * @throws NoSuchFileException If the file does not exist and neither {@link StandardOpenOption#CREATE_NEW} nor {@link StandardOpenOption#CREATE} is
	 *             specified.
	 * @throws IOException In the event of other exceptions.
	 */
	protected abstract OpenResult<R> openFile(final R reference, Set<? extends OpenOption> options) throws FileAlreadyExistsException, IOException, NoSuchFileException;

	@Override
	public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
		final IFunction1<? super R, ? extends IAttributeViewAccessor<A, ?>> factory = getAVARegistry().getByAttributes(type);
		final P checked = check(path);
		return this.<IThrowFunction1<P, A, NoSuchFileException>, NoSuchFileException>wrap1(p -> {
			final R reference = resolve(p);
			return factory.apply(reference).getAttributes();
		}, getSyncFactory().getSyncThrowFunction1(), checked).apply(checked);
	}

	@Override
	public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
		final IFunction1<? super R, ? extends IAttributeViewAccessor<?, ?>> factory = getAVARegistry().getByName(attributes);
		final P checked = check(path);
		return this.<IThrowFunction1<P, Map<String, Object>, NoSuchFileException>, NoSuchFileException>wrap1(p -> {
			final R reference = resolve(p);
			final IAttributeViewAccessor<?, ?> accessor = factory.apply(reference);

			final Map<String, Object> retVal = new LinkedHashMap<>();
			for (String name : accessor.getNames()) {
				retVal.put(name, accessor.get(name));
			}
			return retVal;
		}, getSyncFactory().getSyncThrowFunction1(), checked).apply(checked);
	}

	/**
	 * Resolve the specified path to a reference. This method must not throw an exception even when the path doesn't exist. Instead the reference must handle
	 * that later.
	 * 
	 * This may involve walking a directory tree, or retrieving remote information. We recommend that the reference include information about the parent of the
	 * path.
	 * 
	 * @param path The path to resolve.
	 * @return An internal reference to the path.
	 */
	protected abstract R resolve(P path);

	@Override
	public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
		final FileAttributeName name = new FileAttributeName(attribute);
		final IFunction1<? super R, ? extends IAttributeViewAccessor<?, ?>> factory = getAVARegistry().getByName(name.getAttributes());

		final P checked = check(path);
		this.<IThrowConsumer1<P, NoSuchFileException>, NoSuchFileException>wrap1(p -> {
			final R reference = resolve(p);
			final IAttributeViewAccessor<?, ?> accessor = factory.apply(reference);
			accessor.set(name.getAttribute(), value);
		}, getSyncFactory().getSyncThrowConsumer1(), checked).accept(checked);
	}

	/**
	 * Set the specified attributes on a newly created entry.
	 * 
	 * @param reference A reference to the newly created entry.
	 * @param attrs The attributes to be set.
	 * @throws NoSuchFileException If the reference does not point to an existing entry.
	 */
	protected void setAttributes(final R reference, FileAttribute<?>... attrs) throws NoSuchFileException {
		if ((attrs != null) && (attrs.length > 0)) {
			for (FileAttribute<?> attribute : attrs) {
				final FileAttributeName attributeName = new FileAttributeName(attribute.name());
				final IFunction1<? super R, ? extends IAttributeViewAccessor<?, ?>> factory = getAVARegistry().getByName(attributeName.getAttributes());
				factory.apply(reference).set(attributeName.getAttribute(), attribute.value());
			}
		}
	}

	/**
	 * Convert a reference to a directory into a directory stream.
	 * 
	 * This method will only be called while the appropriate system level locks are held. It is up to the caller to obtain any necessary locks on individual
	 * files.
	 * 
	 * @param reference A reference to the directory itself, which can be used to look up the name of the children.
	 * @param dir The original path specified by the user. Return values should be resolved against this path.
	 * @return
	 * @throws IOException
	 */
	protected abstract DirectoryStream<Path> toDirectoryStream(final R reference, Path dir) throws IOException;

	/**
	 * Wrap a functional for execution inside the locks appropriate to the specified path. Generally you will not need to call this method, as it is called from
	 * the method implementations on {@link AGenericFileSystemProvider}.
	 * 
	 * @param functional The functional to wrap.
	 * @param sync The appropriate sync method (such as {@link IFunction1#sync(Object)}), which can be overridden and extended as described in the javadocs for
	 *            {@link AGenericFileSystemProvider}.
	 * @param path The path to consider for locking purposes.
	 * @return
	 */
	protected <F extends IThrowConsumer1<P, T>, T extends Throwable> F wrap1(F functional, IFunction2<? super F, ? super Object, ? extends F> sync, P path) {
		@SuppressWarnings("unchecked")
		final List<Object> locks = getLocks(path);
		F retVal = functional;
		for (Object lock : locks) {
			retVal = sync.apply(retVal, lock);
		}
		return retVal;
	}

	/**
	 * Wrap a functional for execution inside the locks appropriate to the specified paths. Generally you will not need to call this method, as it is called
	 * from the method implementations on {@link AGenericFileSystemProvider}.
	 * 
	 * @param functional The functional to wrap.
	 * @param sync The appropriate sync method (such as {@link IFunction1#sync(Object)}), which can be overridden and extended as described in the javadocs for
	 *            {@link AGenericFileSystemProvider}.
	 * @param paths The paths to consider for locking purposes. This method will use the natural sort order of <code>Internal</code> to ensure that locks are
	 *            acquired and released in a consistent order.
	 * @return
	 */
	protected <F extends IThrowConsumer1<P[], T>, T extends Throwable> F wrapN(F functional, IFunction2<? super F, ? super Object, ? extends F> sync, @SuppressWarnings("unchecked") P... paths) {
		final List<Object> locks = getLocks(paths);
		F retVal = functional;
		for (Object lock : locks) {
			retVal = sync.apply(retVal, lock);
		}
		return retVal;
	}
}
