package com.g2forge.alexandria.filesystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.NonReadableChannelException;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessDeniedException;
import java.nio.file.AccessMode;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.ProviderMismatchException;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.g2forge.alexandria.analysis.ISerializableFunction1;
import com.g2forge.alexandria.filesystem.attributes.HBasicFileAttributes;
import com.g2forge.alexandria.filesystem.file.FileTimeMatcher;
import com.g2forge.alexandria.filesystem.file.FileTimeTester;
import com.g2forge.alexandria.java.concurrent.HConcurrent;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.math.HMath;
import com.g2forge.alexandria.java.io.RuntimeIOException;
import com.g2forge.alexandria.java.io.file.HFile;
import com.g2forge.alexandria.test.FieldMatcher;
import com.g2forge.alexandria.test.HAssert;
import com.g2forge.alexandria.test.HMatchers;

public abstract class ATestFileSystemProvider {
	protected static final Pattern DISABLE_LAST_ACCESS_PATTERN = Pattern.compile("DisableLastAccess = ([0-9]+).*");

	protected static final ISerializableFunction1<BasicFileAttributes, ?>[] basicFileAttributeFunctions = FieldMatcher.create(BasicFileAttributes::creationTime, BasicFileAttributes::lastModifiedTime, BasicFileAttributes::lastAccessTime, BasicFileAttributes::isDirectory, BasicFileAttributes::isRegularFile, BasicFileAttributes::isSymbolicLink, BasicFileAttributes::isOther, BasicFileAttributes::size);

	protected static final ISerializableFunction1<BasicFileAttributes, ?>[] basicFileAttributeFunctionsAfterAccess = FieldMatcher.create(BasicFileAttributes::creationTime, BasicFileAttributes::lastModifiedTime, BasicFileAttributes::isDirectory, BasicFileAttributes::isRegularFile, BasicFileAttributes::isSymbolicLink, BasicFileAttributes::isOther, BasicFileAttributes::size);

	protected static final ISerializableFunction1<BasicFileAttributes, ?>[] basicFileAttributeFunctionsAfterCopy = FieldMatcher.create(BasicFileAttributes::lastModifiedTime, BasicFileAttributes::isDirectory, BasicFileAttributes::isRegularFile, BasicFileAttributes::isSymbolicLink, BasicFileAttributes::isOther, BasicFileAttributes::size);

	/**
	 * Test if a Java standard file system for the specified <code>path</code> is expected to support last access time. Specifically this method handles the
	 * fact that on Windows the NTFS file system makes access time optional, depending on a configuration setting. Please do not use this method to test your
	 * own file system providers, where you should already know whether they support last access times.
	 * 
	 * This method is conservative in that it expects all file systems to support last access time, unless we specifically know to the contrary. This way unit
	 * tests will fail if our assumptions are wrong, rather than passing when they should not.
	 * 
	 * @param path The path to test for access time support.
	 * @return <code>true</code> if, to the best of our knowledge, the file system underlying the specified path will support last access times.
	 */
	public static boolean isSupportsLastAccess(final Path path) {
		if (!path.getFileSystem().supportedFileAttributeViews().contains("dos")) return true;
		else {
			final ProcessBuilder builder = new ProcessBuilder();
			builder.command("powershell", "fsutil", "behavior", "query", "disablelastaccess");
			builder.directory(path.toFile());
			try {
				final Process process = builder.start();
				try {
					try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
						final Matcher matcher = DISABLE_LAST_ACCESS_PATTERN.matcher(reader.readLine().trim());
						if (!matcher.matches()) return false;
						final int value = Integer.valueOf(matcher.group(1));
						return ((value & 0x1) == 0);
					}
				} finally {
					try {
						process.waitFor();
					} catch (InterruptedException e) {}
				}
			} catch (IOException e) {
				throw new RuntimeIOException(e);
			}
		}
	}

	protected FileSystem fs = null;

	protected void assertChildren(Collection<String> children, Path path) throws IOException {
		final Set<String> actual = HFile.toList(Files.newDirectoryStream(path)).stream().map(p -> p.getFileName().toString()).collect(Collectors.toSet());
		HAssert.assertEquals((children instanceof Set) ? children : new HashSet<>(children), actual);
	}

	@Test
	public void attributes() throws IOException {
		final Path aPath = createPath("/a"), bPath = createPath("a/b.txt");
		Files.createDirectory(aPath);
		Files.newBufferedWriter(bPath).append("Hello, World!").append(System.lineSeparator()).close();

		final BasicFileAttributes aAttributes = Files.readAttributes(aPath, BasicFileAttributes.class);
		HAssert.assertTrue(aAttributes.isDirectory());
		HAssert.assertFalse(aAttributes.isRegularFile());
		HAssert.assertFalse(aAttributes.isOther());
		HAssert.assertFalse(aAttributes.isSymbolicLink());

		final BasicFileAttributes bAttributes = Files.readAttributes(bPath, BasicFileAttributes.class);
		HAssert.assertEquals(bAttributes.lastModifiedTime(), Files.getLastModifiedTime(bPath));
		HAssert.assertFalse(bAttributes.isDirectory());
		HAssert.assertTrue(bAttributes.isRegularFile());
		HAssert.assertFalse(bAttributes.isOther());
		HAssert.assertFalse(bAttributes.isSymbolicLink());

		final Object aFileKey = aAttributes.fileKey();
		if (aFileKey != null) HAssert.assertNotEquals(aFileKey, bAttributes.fileKey());

		final BasicFileAttributeView view = Files.getFileAttributeView(aPath, BasicFileAttributeView.class);
		HAssert.assertThat(view.readAttributes(), new FieldMatcher<>(aAttributes, basicFileAttributeFunctions));

		{
			HConcurrent.wait(10);
			final FileTime value = FileTimeMatcher.now();
			// Delay here to ensure that the written time isn't simply the time at which the attribute is written
			HConcurrent.wait(10);
			view.setTimes(value, aAttributes.lastAccessTime(), aAttributes.creationTime());
			// Allow a slight mismatch as filesystems may not record time to that much precision
			HAssert.assertThat(Long.valueOf(HMath.abs(Duration.between(value.toInstant(), Files.getLastModifiedTime(aPath).toInstant()).toNanos())), Matchers.lessThan(1000l));
		}

		{
			HConcurrent.wait(10);
			final FileTime value = FileTimeMatcher.now();
			// Delay here to ensure that the written time isn't simply the time at which the attribute is written
			HConcurrent.wait(10);
			Files.setAttribute(bPath, "basic:lastModifiedTime", value);
			// Allow a slight mismatch as filesystems may not record time to that much precision
			HAssert.assertThat(Long.valueOf(HMath.abs(Duration.between(value.toInstant(), Files.getLastModifiedTime(bPath).toInstant()).toNanos())), Matchers.lessThan(1000l));
		}
	}

	@Test
	public void badPath() throws IOException {
		HAssert.assertException(ProviderMismatchException.class, "Path \"x\" was not from the same filesystem provider!", () -> fs.provider().checkAccess(Paths.get("x"), AccessMode.READ));
	}

	@Test
	public void copyDirectoryNonEmpty() throws IOException {
		final Path aParent = createPath("x"), bParent = createPath("y");
		final Path a = aParent.resolve("a"), b = bParent.resolve("b");
		Files.createDirectories(a.resolve("0"));
		Files.createDirectories(b.resolve("1"));

		HAssert.<IOException>assertThat(() -> Files.copy(a, b, StandardCopyOption.REPLACE_EXISTING), HMatchers.isThrowable(DirectoryNotEmptyException.class, HMatchers.<String>anyOf(HMatchers.equalTo(String.format("\"%1$s\" is not empty!", b)) /* Helpful error messages */, HMatchers.endsWith(b.getFileName().toString()) /* Machine readable */)));
		assertChildren(HCollection.asList("1"), b);
	}

	@Test
	public void copyDirectoryWithChildren() throws IOException {
		final Path aParent = createPath("x"), bParent = createPath("y");
		final Path a = aParent.resolve("a"), b = bParent.resolve("b");
		Files.createDirectories(a.resolve("0"));
		Files.createDirectories(b);
		assertChildren(HCollection.emptyList(), b);

		HConcurrent.wait(10);
		Files.copy(a, b, StandardCopyOption.REPLACE_EXISTING);
		HAssert.assertTrue(Files.readAttributes(aParent, BasicFileAttributes.class).lastAccessTime().compareTo(Files.readAttributes(bParent, BasicFileAttributes.class).lastModifiedTime()) < 0);

		final BasicFileAttributes aAttributes = Files.readAttributes(a, BasicFileAttributes.class), bAttributes = Files.readAttributes(b, BasicFileAttributes.class);
		HAssert.assertThat(aAttributes, new FieldMatcher<BasicFileAttributes>(bAttributes, BasicFileAttributes::isDirectory, BasicFileAttributes::isRegularFile, BasicFileAttributes::isSymbolicLink, BasicFileAttributes::isOther));
		HAssert.assertTrue(aAttributes.lastModifiedTime().compareTo(bAttributes.lastModifiedTime()) <= 0);

		assertChildren(HCollection.asList("0"), a);
		assertChildren(HCollection.emptyList(), b);
		HAssert.assertTrue(aAttributes.creationTime().compareTo(bAttributes.creationTime()) < 0);
		if (supportLastAccess()) HAssert.assertTrue(aAttributes.lastAccessTime().compareTo(Files.getLastModifiedTime(aParent)) > 0);

		Files.createDirectories(b.resolve("1"));
		assertChildren(HCollection.asList("1"), b);
	}

	@Test
	public void copyFileWithAttributes() throws IOException {
		final Path aParent = createPath("x"), bParent = createPath("y");
		Files.createDirectory(aParent);
		Files.createDirectory(bParent);
		final Path a = aParent.resolve("a"), b = bParent.resolve("b");
		final String content = "Hello, World!";
		Files.newBufferedWriter(a).append(content).append(System.lineSeparator()).close();
		final FileTime pretime = Files.readAttributes(aParent, BasicFileAttributes.class).lastModifiedTime();

		HConcurrent.wait(10);
		Files.copy(a, b, StandardCopyOption.COPY_ATTRIBUTES);
		if (supportLastAccess()) HAssert.assertThat(Files.readAttributes(aParent, BasicFileAttributes.class).lastAccessTime(), Matchers.greaterThanOrEqualTo(pretime));
		HAssert.assertThat(Files.readAttributes(bParent, BasicFileAttributes.class).lastModifiedTime(), Matchers.greaterThan(pretime));
		HAssert.assertThat(Files.readAttributes(b, BasicFileAttributes.class), new FieldMatcher<BasicFileAttributes>(Files.readAttributes(a, BasicFileAttributes.class), basicFileAttributeFunctionsAfterCopy));

		try (final BufferedReader reader = Files.newBufferedReader(b)) {
			HAssert.assertEquals(content, reader.readLine());
		}
	}

	@Test
	public void copySame() throws IOException {
		final Path parent = createPath("/a");
		final Path child = parent.resolve("b");
		Files.createDirectories(child);
		final BasicFileAttributes attributes = Files.readAttributes(parent, BasicFileAttributes.class);

		HConcurrent.wait(10);
		Files.copy(child, child);
		HAssert.assertThat(Files.readAttributes(parent, BasicFileAttributes.class), new FieldMatcher<>(attributes, basicFileAttributeFunctions));
	}

	@Test
	public void createDirectoriesMultiple() throws IOException {
		try (final FileTimeTester a = FileTimeTester.all(createPath("/a")); final FileTimeTester b = FileTimeTester.all(createPath("/a/b")); final FileTimeTester c = FileTimeTester.all(createPath("/a/b/c"))) {
			Files.createDirectories(createPath("/a/b/c"));
		}
		HConcurrent.wait(10);
		try (final FileTimeTester a = FileTimeTester.modify(createPath("/a"), true)) {
			Files.createDirectories(createPath("/a/d"));
		}
		try (final FileTimeTester a = FileTimeTester.read(createPath("/a"), supportLastAccess()); final FileTimeTester b = FileTimeTester.untouched(createPath("/a/b")); final FileTimeTester d = FileTimeTester.untouched(createPath("/a/d"))) {
			assertChildren(HCollection.asList("b", "d"), fs.getPath("/a"));
		}
	}

	@Test
	public void createDirectory() throws IOException {
		assertChildren(HCollection.emptyList(), fs.getPath("/"));
		final FileAttribute<FileTime> expected = HBasicFileAttributes.createLastModifiedTime(FileTimeMatcher.now());
		final Path path = createPath("/a");

		HConcurrent.wait(10);
		try {
			Files.createDirectory(path, expected);
			HAssert.assertEquals(expected.value(), Files.getLastModifiedTime(path));
		} catch (UnsupportedOperationException exception) {
			/** Not all filesystems support this */
			if (!"'basic:lastModifiedTime' not supported as initial attribute".equals(exception.getMessage())) throw exception;
			Files.createDirectory(path);
		}
		assertChildren(HCollection.asList("a"), fs.getPath("/"));
	}

	@Test
	public void createDirectoryExists() throws IOException {
		final Path root = fs.getPath("/");
		assertChildren(HCollection.emptyList(), root);
		final Path a = createPath("/a");
		Files.createDirectory(a);
		HAssert.<IOException>assertThat(() -> Files.createDirectory(a), HMatchers.isThrowable(FileAlreadyExistsException.class, HMatchers.anyOf(HMatchers.equalTo(String.format("\"/%1$s\" already exists!", a.getFileName())), HMatchers.endsWith(a.getFileName().toString()))));
		assertChildren(HCollection.asList(a.getFileName().toString()), root);
	}

	@Test
	public void createDirectoryMissingAncestor() throws IOException {
		assertChildren(HCollection.emptyList(), fs.getPath("/"));
		HAssert.<IOException>assertThat(() -> Files.createDirectory(createPath("/a/b")), HMatchers.isThrowable(NoSuchFileException.class, HMatchers.anyOf(HMatchers.equalTo(String.format("Ancestor \"/a\" does not exist!")), HMatchers.endsWith("b"))));
		assertChildren(HCollection.emptyList(), fs.getPath("/"));
	}

	protected abstract Path createPath(String absolute);

	@Test
	public void deleteDirectoryEmpty() throws IOException {
		final Path path = createPath("/a");
		Files.createDirectory(path);
		HAssert.assertTrue(Files.exists(path));
		Files.delete(path);
		HAssert.assertFalse(Files.exists(path));
	}

	@Test
	public void deleteDirectoryNonEmpty() throws IOException {
		final Path path = createPath("/a");
		Files.createDirectories(createPath("/a/b"));
		HAssert.assertTrue(Files.exists(path));
		HAssert.<IOException>assertThat(() -> Files.delete(path), HMatchers.isThrowable(DirectoryNotEmptyException.class, HMatchers.anyOf(HMatchers.equalTo(String.format("\"/%1$s\" is not empty!", path.getFileName())), HMatchers.endsWith(path.getFileName().toString()))));
		HAssert.assertTrue(Files.exists(path));
	}

	@Test
	public void deleteFile() throws IOException {
		final Path path = createPath("a");
		Files.newBufferedWriter(path).append("a").close();
		HAssert.assertTrue(Files.exists(path));
		Files.delete(path);
		HAssert.assertFalse(Files.exists(path));
	}

	@Test
	public void deleteNonExistant() throws IOException {
		final Path path = createPath("a");
		HAssert.<IOException>assertThat(() -> Files.delete(path), HMatchers.isThrowable(NoSuchFileException.class, HMatchers.anyOf(HMatchers.equalTo(String.format("\"%1$s\" does not exist!", path.getFileName())), HMatchers.endsWith(path.getFileName().toString()))));
	}

	@Test
	public void fileAppend() throws IOException {
		final Path path = createPath("a.txt");
		final String[] lines = { "abc", "def", "ghi" };
		for (int i = 0; i < lines.length; i++) {
			try (final FileTimeTester tester = i == 0 ? FileTimeTester.createModify(path, supportLastAccess()) : FileTimeTester.modify(path, supportLastAccess())) {
				Files.newBufferedWriter(path, StandardOpenOption.APPEND, StandardOpenOption.CREATE).append(lines[i]).append(System.lineSeparator()).close();
			}

			try (final FileTimeTester tester = FileTimeTester.read(path, supportLastAccess())) {
				try (final BufferedReader reader = Files.newBufferedReader(path)) {
					for (int j = 0; j <= i; j++) {
						final String line;
						try {
							line = reader.readLine();
						} catch (IOException exception) {
							HAssert.fail(String.format("Failed to read line %1$d after writing line %2$d: %3$s", j, i, exception.getMessage()));
							return;
						}
						HAssert.assertEquals(String.format("Failed to read line %1$d after writing line %2$d", j, i), lines[j], line);
					}
				}
			}
		}
	}

	@Test
	public void fileCreateExistingFail() throws IOException {
		Files.createDirectory(createPath("/a"));
		final Path a = createPath("a"), b = createPath("b");
		HAssert.<IOException>assertThat(() -> Files.newBufferedWriter(a, StandardOpenOption.CREATE_NEW).close(), HMatchers.anyOf(HMatchers.isThrowable(FileAlreadyExistsException.class, "\"a\" already exists!"), HMatchers.isThrowable(AccessDeniedException.class, HMatchers.endsWith(a.getFileName().toString()))));
		Files.newBufferedWriter(b).append("b").close();
		HAssert.<IOException>assertThat(() -> Files.newBufferedWriter(b, StandardOpenOption.CREATE_NEW).close(), HMatchers.anyOf(HMatchers.isThrowable(FileAlreadyExistsException.class, "\"b\" already exists!"), HMatchers.isThrowable(FileAlreadyExistsException.class, HMatchers.endsWith(b.getFileName().toString()))));
	}

	@Test
	public void fileCreationTime() throws IOException {
		final FileTime expected = FileTimeMatcher.now();
		HConcurrent.wait(100);
		final Path path = createPath("a.txt");
		try (final FileTimeTester a = FileTimeTester.modify(path, supportLastAccess())) {
			Files.newByteChannel(path, HCollection.asSet(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE), HBasicFileAttributes.createCreationTime(expected)).close();
		} catch (UnsupportedOperationException exception) {
			/** Not all filesystems support this */
			if ("'basic:creationTime' not supported as initial attribute".equals(exception.getMessage())) return;
		}

		final BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
		HAssert.assertEquals(expected, attributes.creationTime());
		HAssert.assertTrue(attributes.lastAccessTime().compareTo(expected) > 0);
	}

	@Test
	public void fileLoopback() throws IOException {
		final Path path = createPath("a.txt");
		final String content = "Hello, World!";

		try (final FileTimeTester tester = FileTimeTester.all(path)) {
			Files.newBufferedWriter(path).append(content).append(System.lineSeparator()).close();
		}

		try (final FileTimeTester tester = FileTimeTester.read(path, supportLastAccess())) {
			try (final BufferedReader reader = Files.newBufferedReader(path)) {
				HAssert.assertEquals(content, reader.readLine());
			}
		}
	}

	@Test
	public void fileOptionAppend() throws IOException {
		final Path path = createPath("a.txt");
		Files.newBufferedWriter(path).append("Hello, World!").append(System.lineSeparator()).close();

		// Note that not all file system providers will support read and append
		try (final SeekableByteChannel channel = Files.newByteChannel(path, StandardOpenOption.READ, StandardOpenOption.APPEND)) {
			HAssert.assertEquals(0, channel.position());
		} catch (IllegalArgumentException exception) {
			HAssert.assertEquals("READ + APPEND not allowed", exception.getMessage());
		}

		try (final SeekableByteChannel channel = Files.newByteChannel(path, StandardOpenOption.WRITE, StandardOpenOption.APPEND)) {
			HAssert.assertNotEquals(0, channel.position());
		}
	}

	@Test
	public void fileOptionTruncate() throws IOException {
		final Path path = createPath("a.txt");
		Files.newBufferedWriter(path).append("Hello, World!").append(System.lineSeparator()).close();
		try (final SeekableByteChannel channel = Files.newByteChannel(path, StandardOpenOption.READ, StandardOpenOption.TRUNCATE_EXISTING)) {
			HAssert.assertEquals(0, channel.position());
			HAssert.assertNotEquals(0, channel.size());
		}
		try (final SeekableByteChannel channel = Files.newByteChannel(path, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
			HAssert.assertEquals(0, channel.position());
			HAssert.assertEquals(0, channel.size());
		}
	}

	@Test
	public void fileReadOnly() throws IOException {
		final Path path = createPath("a.txt");
		Files.newByteChannel(path, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE).close();
		try (final SeekableByteChannel channel = Files.newByteChannel(path, StandardOpenOption.READ)) {
			HAssert.assertException(NonWritableChannelException.class, null, () -> channel.write(ByteBuffer.wrap(new byte[] { 0, 1, 2 })));
		}
	}

	@Test
	public void fileSeekAndRead() throws IOException {
		Files.createDirectory(createPath("/a"));
		final Path path = createPath("a/b.txt");
		final String content = "Hello, World!";
		try (final FileTimeTester tester = FileTimeTester.all(path)) {
			Files.newBufferedWriter(path).append(content).append(System.lineSeparator()).close();
		}

		try (final FileTimeTester tester = FileTimeTester.read(path, supportLastAccess())) {
			try (final SeekableByteChannel channel = Files.newByteChannel(path)) {
				channel.position(7);
				try (final BufferedReader reader = new BufferedReader(new InputStreamReader(Channels.newInputStream(channel)))) {
					HAssert.assertEquals("World!", reader.readLine());
				}
			}
		}
	}

	@Test
	public void fileTruncateOverAppend() throws IOException {
		final Path path = createPath("a.txt");
		Files.newBufferedWriter(path).append("Hello, World!").append(System.lineSeparator()).close();
		try (final SeekableByteChannel channel = Files.newByteChannel(path, StandardOpenOption.WRITE, StandardOpenOption.APPEND, StandardOpenOption.TRUNCATE_EXISTING)) {
			HAssert.assertEquals(0, channel.position());
			HAssert.assertEquals(0, channel.size());
		} catch (IllegalArgumentException exception) {
			// It's ALSO allowed for the file system provide to just reject this, of course...
			HAssert.assertEquals("APPEND + TRUNCATE_EXISTING not allowed", exception.getMessage());
		}
	}

	@Test
	public void fileWriteOnly() throws IOException {
		try (final SeekableByteChannel channel = Files.newByteChannel(createPath("a.txt"), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
			HAssert.assertException(NonReadableChannelException.class, null, () -> channel.read(ByteBuffer.allocate(0)));
		}
	}

	@Test
	public void fileWritePastEnd() throws IOException {
		final Path path = createPath("a.txt");

		try (final SeekableByteChannel channel = Files.newByteChannel(path, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
			channel.write(ByteBuffer.wrap(new byte[] { 1, 2 }));
			channel.position(5);
			channel.write(ByteBuffer.wrap(new byte[] { 3, 4 }));
		}

		try (final SeekableByteChannel channel = Files.newByteChannel(path)) {
			final ByteBuffer buffer = ByteBuffer.allocate(7);
			channel.read(buffer);
			HAssert.assertArrayEquals(new byte[] { 1, 2, 0, 0, 0, 3, 4 }, buffer.array());
		}
	}

	@Test
	public void moveAtomic() throws IOException {
		final Path aParent = createPath("x"), bParent = createPath("y");
		Files.createDirectory(aParent);
		Files.createDirectory(bParent);
		final Path a = aParent.resolve("a"), b = bParent.resolve("b");
		final String content = "Hello, World!";
		Files.newBufferedWriter(a).append(content).append(System.lineSeparator()).close();
		final BasicFileAttributes attributes = Files.readAttributes(a, BasicFileAttributes.class);
		final FileTime originalModifiedTime = Files.getLastModifiedTime(aParent);

		HConcurrent.wait(10);
		Files.move(a, b, StandardCopyOption.ATOMIC_MOVE);
		HAssert.assertTrue(originalModifiedTime.compareTo(Files.getLastModifiedTime(aParent)) < 0);
		HAssert.assertEquals(Files.getLastModifiedTime(aParent), Files.getLastModifiedTime(bParent));

		try (final BufferedReader reader = Files.newBufferedReader(b)) {
			HAssert.assertEquals(content, reader.readLine());
		}
		HAssert.assertThat(Files.readAttributes(b, BasicFileAttributes.class), new FieldMatcher<>(attributes, basicFileAttributeFunctionsAfterAccess));
	}

	@Test
	public void moveDirectoryNonEmpty() throws IOException {
		final Path a = createPath("a"), b = createPath("b");
		Files.createDirectory(a);
		Files.createDirectories(b.resolve("1"));
		HAssert.<IOException>assertThat(() -> Files.move(a, b, StandardCopyOption.REPLACE_EXISTING), HMatchers.isThrowable(DirectoryNotEmptyException.class, HMatchers.<String>anyOf(HMatchers.equalTo(String.format("\"%1$s\" is not empty!", b)) /* Helpful error messages */, HMatchers.endsWith(b.toString()) /* Machine readable */)));
		HAssert.assertTrue(Files.isDirectory(a));
	}

	@Test
	public void moveExistingFail() throws IOException {
		final Path a = createPath("a"), b = createPath("b");
		Files.createDirectory(a);
		Files.newBufferedWriter(b).append("Hello, World!").append(System.lineSeparator()).close();
		HAssert.<IOException>assertThat(() -> Files.move(a, b), HMatchers.isThrowable(FileAlreadyExistsException.class, HMatchers.<String>anyOf(HMatchers.equalTo(String.format("\"%1$s\" already exists!", b)) /* Helpful error messages */, HMatchers.endsWith(b.toString()) /* Machine readable */)));
		HAssert.assertTrue(Files.isRegularFile(b));
	}

	@Test
	public void moveExistingReplace() throws IOException {
		final Path a = createPath("a"), b = createPath("b");
		Files.createDirectory(a);
		Files.newBufferedWriter(b).append("Hello, World!").append(System.lineSeparator()).close();
		Files.move(a, b, StandardCopyOption.REPLACE_EXISTING);
		HAssert.assertTrue(Files.isDirectory(b));
	}

	@Test
	public void moveNonAtomic() throws IOException {
		final Path a = createPath("a"), b = createPath("b");
		final String content = "Hello, World!";
		Files.newBufferedWriter(a).append(content).append(System.lineSeparator()).close();
		final BasicFileAttributes attributes = Files.readAttributes(a, BasicFileAttributes.class);

		Files.move(a, b);

		try (final BufferedReader reader = Files.newBufferedReader(b)) {
			HAssert.assertEquals(content, reader.readLine());
		}
		HAssert.assertThat(Files.readAttributes(b, BasicFileAttributes.class), new FieldMatcher<>(attributes, basicFileAttributeFunctionsAfterAccess));
	}

	@Test
	public void moveSame() throws IOException {
		final Path parent = createPath("/a");
		final Path child = parent.resolve("b");
		Files.createDirectories(child);
		final BasicFileAttributes attributes = Files.readAttributes(parent, BasicFileAttributes.class);

		HConcurrent.wait(10);
		Files.move(child, child);
		HAssert.assertThat(Files.readAttributes(parent, BasicFileAttributes.class), new FieldMatcher<>(attributes, basicFileAttributeFunctions));
	}

	@Test
	public void pathResolve() {
		HAssert.assertEquals(createPath("a/b"), createPath("a").resolve(Paths.get("b")));

		// If the filesystem of the OS we're on has a root directory, use it to test absolute path resolution
		final Iterator<Path> iterator = FileSystems.getDefault().getRootDirectories().iterator();
		if (iterator.hasNext()) {
			final Path b = iterator.next().resolve("b");
			HAssert.assertSame(b, createPath("a").resolve(b));
		}
	}

	@Test
	public void resolveEmptyChild() throws IOException {
		final Path emptyPath = Paths.get("");
		final Path directory = createPath("").resolve(emptyPath);
		HAssert.assertTrue(Files.isDirectory(directory));
	}

	/**
	 * Test if this file system is expected to support last access timestampes. This allows {@link ATestFileSystemProvider} to test file systems either way.
	 * Note that the return value of this method may vary from test to test.
	 * 
	 * @return <code>true</code> if the file system being used for this test is expected to support last access time.
	 * @see #isSupportsLastAccess(Path)
	 */
	protected boolean supportLastAccess() {
		return true;
	}
}
