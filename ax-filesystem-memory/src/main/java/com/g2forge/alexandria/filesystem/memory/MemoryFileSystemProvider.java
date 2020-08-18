package com.g2forge.alexandria.filesystem.memory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.ProviderMismatchException;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.g2forge.alexandria.annotations.service.Service;
import com.g2forge.alexandria.filesystem.AGenericFileSystemProvider;
import com.g2forge.alexandria.filesystem.attributes.IGenericBasicAttributeModifier;
import com.g2forge.alexandria.filesystem.attributes.accessor.AttributeViewAccessorRegistry;
import com.g2forge.alexandria.filesystem.file.GenericEntryReference;
import com.g2forge.alexandria.filesystem.memory.attributes.BasicAttributeViewAccessor;
import com.g2forge.alexandria.filesystem.memory.attributes.BasicAttributes;
import com.g2forge.alexandria.filesystem.memory.file.Directory;
import com.g2forge.alexandria.filesystem.memory.file.EntryAccessor;
import com.g2forge.alexandria.filesystem.memory.file.File;
import com.g2forge.alexandria.filesystem.memory.file.FileSeekableByteChannel;
import com.g2forge.alexandria.filesystem.memory.file.IEntry;
import com.g2forge.alexandria.filesystem.path.FileSystemPathURI;
import com.g2forge.alexandria.filesystem.path.GenericFileSystem;
import com.g2forge.alexandria.filesystem.path.GenericPath;
import com.g2forge.alexandria.filesystem.path.IGenericFileSystemProviderInternal;
import com.g2forge.alexandria.java.adt.MappedIterator;
import com.g2forge.alexandria.java.adt.compare.ComparableComparator;
import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Memory file system URIs are of the format "memory:filesystem!path", this allows it to provide multiple independent filesystems.
 */
@Service(FileSystemProvider.class)
public class MemoryFileSystemProvider extends AGenericFileSystemProvider<GenericPath, MemoryFileSystemProvider.Internal, GenericEntryReference<IEntry, File, Directory, GenericPath>> {
	@RequiredArgsConstructor
	@Getter
	protected class Internal implements IGenericFileSystemProviderInternal<GenericPath>, Comparable<Internal> {
		protected final String key;

		protected final Directory root = createDirectory();

		@Override
		public FileSystemProvider asFileSystemProvider() {
			return MemoryFileSystemProvider.this;
		}

		@Override
		public void close() {
			synchronized (fileSystems) {
				fileSystems.remove(getKey());
			}
		}

		@Override
		public int compareTo(Internal o) {
			return getKey().compareTo(o.getKey());
		}

		protected Directory createDirectory() {
			return new Directory(BasicAttributes.createNow(), new TreeMap<>());
		}

		@Override
		public Set<String> getSupportedFileAttributeViews() {
			return getAVARegistry().getAttributesNames();
		}

		@Override
		public URI toURI(GenericPath path) throws URISyntaxException {
			return new FileSystemPathURI(getScheme(), getKey(), path.toAbsolutePath().toString()).toURI();
		}
	}

	protected static final AttributeViewAccessorRegistry<GenericEntryReference<IEntry, File, Directory, GenericPath>> avaRegistry = new AttributeViewAccessorRegistry<GenericEntryReference<IEntry, File, Directory, GenericPath>>().register(BasicAttributeViewAccessor::new);

	protected final Map<String, GenericFileSystem> fileSystems = new HashMap<>();

	protected GenericPath check(Path path) {
		if (path == null) throw new NullPointerException();
		if (!(path instanceof GenericPath)) throw new ProviderMismatchException(String.format("Path \"%1$s\" was not from the same filesystem provider!", path));
		return ((GenericPath) path).check(provider -> (provider instanceof Internal) && (((Internal) provider).asFileSystemProvider() == this));
	}

	protected void checkAccess(GenericEntryReference<IEntry, File, Directory, GenericPath> reference, AccessMode... modes) throws IOException {
		if (!HCollection.difference(HCollection.asSet(modes), HCollection.asSet(AccessMode.READ, AccessMode.WRITE, AccessMode.EXECUTE)).isEmpty()) throw new UnsupportedOperationException();
		reference.assertExists();
	}

	@Override
	protected void checkReplaceable(GenericEntryReference<IEntry, File, Directory, GenericPath> reference) throws IOException {
		if (!reference.isFullyResolved()) return;
		final IEntry entry = reference.getEntry();
		if (entry instanceof File) return;
		if (entry instanceof Directory) {
			if (!((Directory) entry).getEntries().isEmpty()) throw new DirectoryNotEmptyException(String.format("\"%1$s\" is not empty!", reference.getResolved()));
			return;
		}
		throw new IOException("Unrecognized entry type!");
	}

	protected CopyResult<GenericEntryReference<IEntry, File, Directory, GenericPath>> copy(GenericEntryReference<IEntry, File, Directory, GenericPath> refSource, CopyOption... options) throws IOException {
		refSource.assertExists();
		final Set<CopyOption> optionSet = HCollection.asSet(options);

		final boolean copyAttributes = optionSet.contains(StandardCopyOption.COPY_ATTRIBUTES);
		final IEntry entry = refSource.getEntry().copy(copyAttributes, TreeMap::new);

		return new CopyResult<>(refSource.getEntry().getBasicAttributes(), entry.getBasicAttributes(), refTarget -> {
			final String name = getCopyTargetName(optionSet, refTarget);
			refTarget.getParent().getEntries().put(name, entry);
			return refTarget.getParent().getBasicAttributes();
		});
	}

	@Override
	protected FileSeekableByteChannel createChannel(final boolean read, final boolean write, final GenericEntryReference<IEntry, File, Directory, GenericPath> reference) throws IOException {
		return new FileSeekableByteChannel(read || !write, write, reference.asFile());
	}

	@Override
	protected OpenResult<GenericEntryReference<IEntry, File, Directory, GenericPath>> createDirectory(final GenericEntryReference<IEntry, File, Directory, GenericPath> reference) throws FileAlreadyExistsException, NoSuchFileException {
		reference.asssertNotExists();
		if (reference.getRemaining().getNameCount() != 1) throw new NoSuchFileException(String.format("Ancestor \"%1$s\" does not exist!", reference.getResolved().resolve(reference.getRemaining().getName(0))));
		final String name = reference.getRemaining().getFileName().toString();
		final Directory directory = getInternal(reference.getResolved()).createDirectory();
		reference.getParent().getEntries().put(name, directory);
		return new OpenResult<>(reference.getParent().getBasicAttributes(), directory.getBasicAttributes(), reference.finish());
	}

	@Override
	protected IGenericBasicAttributeModifier delete(final GenericEntryReference<IEntry, File, Directory, GenericPath> reference) throws NoSuchFileException, DirectoryNotEmptyException {
		reference.assertExists();

		final String name = reference.getResolved().getFileName().toString();
		final Directory parent = reference.getParent();
		final IEntry child = parent.getEntries().get(name);
		if ((child instanceof Directory) && !((Directory) child).getEntries().isEmpty()) throw new DirectoryNotEmptyException(String.format("\"%1$s\" is not empty!", reference.getResolved()));
		parent.getEntries().remove(name);
		return parent.getBasicAttributes();
	}

	@Override
	protected AttributeViewAccessorRegistry<? super GenericEntryReference<IEntry, File, Directory, GenericPath>> getAVARegistry() {
		return avaRegistry;
	}

	protected String getCopyTargetName(final Set<CopyOption> optionSet, final GenericEntryReference<IEntry, File, Directory, GenericPath> reference) throws IOException, FileAlreadyExistsException {
		if (optionSet.contains(StandardCopyOption.REPLACE_EXISTING)) {
			if (reference.isFullyResolved()) {
				final String name = reference.getResolved().getFileName().toString();
				reference.getParent().getEntries().remove(name);
				return name;
			}
		} else reference.asssertNotExists();
		return reference.getCreateName();
	}

	@Override
	public FileStore getFileStore(Path path) throws IOException {
		return null;
	}

	@Override
	public FileSystem getFileSystem(URI uri) {
		final FileSystemPathURI memoryURI = new FileSystemPathURI(getScheme(), uri);
		final String key = memoryURI.getFileSystem();
		synchronized (fileSystems) {
			return fileSystems.get(key);
		}
	}

	@Override
	protected Internal getInternal(GenericPath checked) {
		return (Internal) checked.getGenericFileSystem().getProvider();
	}

	@Override
	protected List<Object> getLocks(Collection<? extends GenericPath> paths) {
		return paths.stream().map(this::getInternal).sorted(ComparableComparator.create()).distinct().collect(Collectors.toList());
	}

	@Override
	protected List<Object> getLocks(GenericPath path) {
		return HCollection.asList(getInternal(path));
	}

	@Override
	public Path getPath(URI uri) {
		final FileSystemPathURI memoryURI = new FileSystemPathURI(getScheme(), uri);
		final String key = memoryURI.getFileSystem();
		final GenericFileSystem fileSystem;
		synchronized (fileSystems) {
			fileSystem = fileSystems.get(key);
		}
		if (fileSystem == null) throw new NullPointerException(String.format("Memory file system \"%1$s\" does not exist!", key));
		return fileSystem.getPath(memoryURI.getPath());
	}

	@Override
	public String getScheme() {
		return "memory";
	}

	@Override
	public boolean isHidden(Path path) throws IOException {
		return false;
	}

	@Override
	public boolean isSameFile(Path path0, Path path1) throws IOException {
		final GenericPath abs0 = check(path0).toAbsolutePath();
		final GenericPath abs1 = check(path1).toAbsolutePath();
		return abs0.equals(abs1);
	}

	@Override
	protected CopyResult<GenericEntryReference<IEntry, File, Directory, GenericPath>> move(GenericEntryReference<IEntry, File, Directory, GenericPath> refSource, CopyOption... options) throws IOException {
		refSource.assertExists();
		final IEntry entry = refSource.getParent().getEntries().remove(refSource.getResolved().getFileName().toString());

		return new CopyResult<>(refSource.getParent().getBasicAttributes(), null, refTarget -> {
			final String name = getCopyTargetName(HCollection.asSet(options), refTarget);
			refTarget.getParent().getEntries().put(name, entry);
			return refTarget.getParent().getBasicAttributes();
		});
	}

	@Override
	public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
		final FileSystemPathURI memoryURI = new FileSystemPathURI(getScheme(), uri);
		if ((memoryURI.getPath() != null) && !memoryURI.getPath().isEmpty()) throw new IllegalArgumentException("Cannot create a file system from a URI with a non-empty path!");
		final String key = memoryURI.getFileSystem();
		synchronized (fileSystems) {
			if (fileSystems.containsKey(key)) throw new FileSystemAlreadyExistsException();
			final GenericFileSystem retVal = new GenericFileSystem(new Internal(key), env);
			fileSystems.put(key, retVal);
			return retVal;
		}
	}

	@Override
	protected OpenResult<GenericEntryReference<IEntry, File, Directory, GenericPath>> openFile(final GenericEntryReference<IEntry, File, Directory, GenericPath> reference, Set<? extends OpenOption> options) throws FileAlreadyExistsException, IOException, NoSuchFileException {
		final boolean createNew = options.contains(StandardOpenOption.CREATE_NEW);
		final boolean create = options.contains(StandardOpenOption.CREATE);
		if (createNew || create) {
			if (createNew || !reference.isFullyResolved()) {
				if (createNew) reference.asssertNotExists();
				final String name = reference.getCreateName();

				final File file = new File(BasicAttributes.createNow(), new byte[0]);
				reference.getParent().getEntries().put(name, file);
				return new OpenResult<>(reference.getParent().getBasicAttributes(), file.getBasicAttributes(), reference.finish());
			}
		} else reference.assertExists();
		return new OpenResult<>(reference.getParent().getBasicAttributes(), null, null);
	}

	@Override
	protected GenericEntryReference<IEntry, File, Directory, GenericPath> resolve(GenericPath path) {
		return new GenericEntryReference<>(EntryAccessor.create(), this::check, getInternal(path).getRoot(), path);
	}

	protected DirectoryStream<Path> toDirectoryStream(final GenericEntryReference<IEntry, File, Directory, GenericPath> reference, Path dir) throws IOException {
		final Directory directory = reference.asDirectory();
		final Set<String> names = new LinkedHashSet<>(directory.getEntries().keySet());
		directory.getBasicAttributes().access(null);
		return new DirectoryStream<Path>() {
			@Override
			public void close() throws IOException {}

			@Override
			public Iterator<Path> iterator() {
				return new MappedIterator<String, Path>(names.iterator(), dir::resolve);
			}
		};
	}
}
