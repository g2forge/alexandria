package com.g2forge.alexandria.filesystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.NonReadableChannelException;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
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
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.analysis.ISerializableFunction1;
import com.g2forge.alexandria.filesystem.attributes.HBasicFileAttributes;
import com.g2forge.alexandria.filesystem.file.FileTimeMatcher;
import com.g2forge.alexandria.filesystem.file.FileTimeTester;
import com.g2forge.alexandria.java.concurrent.HConcurrent;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.io.HFile;
import com.g2forge.alexandria.test.FieldMatcher;
import com.g2forge.alexandria.test.HAssert;

public abstract class ATestFileSystemProvider {
	protected static final ISerializableFunction1<BasicFileAttributes, ?>[] basicFileAttributeFunctions = FieldMatcher.create(BasicFileAttributes::creationTime, BasicFileAttributes::lastModifiedTime, BasicFileAttributes::lastAccessTime, BasicFileAttributes::isDirectory, BasicFileAttributes::isRegularFile, BasicFileAttributes::isSymbolicLink, BasicFileAttributes::isOther, BasicFileAttributes::size);

	protected static List<String> getChildNames(final Path path) throws IOException {
		return HFile.toList(Files.newDirectoryStream(path)).stream().map(p -> p.getFileName().toString()).collect(Collectors.toList());
	}

	protected FileSystem fs = null;

	@Test
	public void attributes() throws IOException {
		final Path aPath = createPath("/a"), bPath = createPath("a/b.txt");
		Files.createDirectory(aPath);
		Files.newBufferedWriter(bPath).append("Hello, World!").append(System.lineSeparator()).close();

		final BasicFileAttributes aAttributes = Files.readAttributes(aPath, BasicFileAttributes.class);
		Assert.assertTrue(aAttributes.isDirectory());
		Assert.assertFalse(aAttributes.isRegularFile());
		Assert.assertFalse(aAttributes.isOther());
		Assert.assertFalse(aAttributes.isSymbolicLink());

		final BasicFileAttributes bAttributes = Files.readAttributes(bPath, BasicFileAttributes.class);
		Assert.assertEquals(bAttributes.lastModifiedTime(), Files.getLastModifiedTime(bPath));
		Assert.assertFalse(bAttributes.isDirectory());
		Assert.assertTrue(bAttributes.isRegularFile());
		Assert.assertFalse(bAttributes.isOther());
		Assert.assertFalse(bAttributes.isSymbolicLink());

		Assert.assertNotEquals(aAttributes.fileKey(), bAttributes.fileKey());

		final BasicFileAttributeView view = Files.getFileAttributeView(aPath, BasicFileAttributeView.class);
		HAssert.assertThat(view.readAttributes(), new FieldMatcher<>(aAttributes, basicFileAttributeFunctions));

		{
			HConcurrent.wait(10);
			final FileTime value = FileTimeMatcher.now();
			view.setTimes(value, aAttributes.lastAccessTime(), aAttributes.creationTime());
			HAssert.assertEquals(value, Files.getLastModifiedTime(aPath));
		}

		{
			HConcurrent.wait(10);
			final FileTime value = FileTimeMatcher.now();
			Files.setAttribute(bPath, "basic:lastModifiedTime", value);
			HAssert.assertEquals(value, Files.getLastModifiedTime(bPath));
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
		HAssert.assertEquals(HCollection.asList("1"), getChildNames(b));

		HConcurrent.wait(10);
		Files.copy(a, b, StandardCopyOption.REPLACE_EXISTING);
		HAssert.assertTrue(Files.readAttributes(aParent, BasicFileAttributes.class).lastAccessTime().compareTo(Files.readAttributes(bParent, BasicFileAttributes.class).lastModifiedTime()) < 0);

		final BasicFileAttributes aAttributes = Files.readAttributes(a, BasicFileAttributes.class), bAttributes = Files.readAttributes(b, BasicFileAttributes.class);
		HAssert.assertThat(aAttributes, new FieldMatcher<BasicFileAttributes>(bAttributes, BasicFileAttributes::lastModifiedTime, BasicFileAttributes::isDirectory, BasicFileAttributes::isRegularFile, BasicFileAttributes::isSymbolicLink, BasicFileAttributes::isOther));

		HAssert.assertEquals(HCollection.asList("0"), getChildNames(a));
		HAssert.assertEquals(HCollection.emptyList(), getChildNames(b));
		HAssert.assertTrue(aAttributes.creationTime().compareTo(bAttributes.creationTime()) < 0);
		HAssert.assertTrue(aAttributes.lastAccessTime().compareTo(Files.getLastModifiedTime(aParent)) > 0);
	}

	@Test
	public void copyFileWithAttributes() throws IOException {
		final Path aParent = createPath("x"), bParent = createPath("y");
		Files.createDirectory(aParent);
		Files.createDirectory(bParent);
		final Path a = aParent.resolve("a"), b = bParent.resolve("b");
		final String content = "Hello, World!";
		Files.newBufferedWriter(a).append(content).append(System.lineSeparator()).close();

		HConcurrent.wait(10);
		Files.copy(a, b, StandardCopyOption.COPY_ATTRIBUTES);
		HAssert.assertTrue(Files.readAttributes(aParent, BasicFileAttributes.class).lastAccessTime().compareTo(Files.readAttributes(bParent, BasicFileAttributes.class).lastModifiedTime()) < 0);
		HAssert.assertThat(Files.readAttributes(b, BasicFileAttributes.class), new FieldMatcher<BasicFileAttributes>(Files.readAttributes(a, BasicFileAttributes.class), basicFileAttributeFunctions));

		try (final BufferedReader reader = Files.newBufferedReader(b)) {
			Assert.assertEquals(content, reader.readLine());
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
		try (final FileTimeTester a = FileTimeTester.modify(createPath("/a"))) {
			Files.createDirectories(createPath("/a/d"));
		}
		try (final FileTimeTester a = FileTimeTester.read(createPath("/a")); final FileTimeTester b = FileTimeTester.untouched(createPath("/a/b")); final FileTimeTester d = FileTimeTester.untouched(createPath("/a/d"))) {
			Assert.assertEquals(HCollection.asList("b", "d"), getChildNames(fs.getPath("/a")));
		}
	}

	@Test
	public void createDirectory() throws IOException {
		HAssert.assertTrue(HFile.toList(Files.newDirectoryStream(fs.getPath("/"))).isEmpty());
		final FileAttribute<FileTime> expected = HBasicFileAttributes.createLastModifiedTime(FileTimeMatcher.now());
		final Path path = createPath("/a");

		HConcurrent.wait(10);
		Files.createDirectory(path, expected);
		HAssert.assertEquals(expected.value(), Files.getLastModifiedTime(path));
		HAssert.assertEquals(HCollection.asList("a"), getChildNames(fs.getPath("/")));
	}

	@Test
	public void createDirectoryExists() throws IOException {
		Assert.assertTrue(HFile.toList(Files.newDirectoryStream(fs.getPath("/"))).isEmpty());
		Files.createDirectory(createPath("/a"));
		HAssert.assertException(FileAlreadyExistsException.class, "\"/a\" already exists!", () -> Files.createDirectory(createPath("/a")));
		Assert.assertEquals(HCollection.asList("a"), getChildNames(fs.getPath("/")));
	}

	@Test
	public void createDirectoryMissingAncestor() throws IOException {
		Assert.assertEquals(HCollection.emptyList(), HFile.toList(Files.newDirectoryStream(fs.getPath("/"))));
		HAssert.assertException(NoSuchFileException.class, "Ancestor \"/a\" does not exist!", () -> Files.createDirectory(createPath("/a/b")));
		Assert.assertEquals(HCollection.emptyList(), getChildNames(fs.getPath("/")));
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
		HAssert.assertException(DirectoryNotEmptyException.class, "\"/a\" is not empty!", () -> Files.delete(path));
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
		HAssert.assertException(NoSuchFileException.class, "\"a\" does not exist!", () -> Files.delete(createPath("a")));
	}

	@Test
	public void fileAppend() throws IOException {
		final Path path = createPath("a.txt");
		final String[] lines = { "abc", "def", "ghi" };
		for (int i = 0; i < lines.length; i++) {
			try (final FileTimeTester tester = i == 0 ? FileTimeTester.all(path) : FileTimeTester.modify(path)) {
				Files.newBufferedWriter(path, StandardOpenOption.APPEND, StandardOpenOption.CREATE).append(lines[i]).append(System.lineSeparator()).close();
			}

			try (final FileTimeTester tester = FileTimeTester.read(path)) {
				try (final BufferedReader reader = Files.newBufferedReader(path)) {
					for (int j = 0; j <= i; j++) {
						final String line;
						try {
							line = reader.readLine();
						} catch (IOException exception) {
							Assert.fail(String.format("Failed to read line %1$d after writing line %2$d: %3$s", j, i, exception.getMessage()));
							return;
						}
						Assert.assertEquals(String.format("Failed to read line %1$d after writing line %2$d", j, i), lines[j], line);
					}
				}
			}
		}
	}

	@Test
	public void fileCreateExistingFail() throws IOException {
		Files.createDirectory(createPath("/a"));
		final Path a = createPath("a"), b = createPath("b");
		HAssert.assertException(FileAlreadyExistsException.class, "\"a\" already exists!", () -> Files.newBufferedWriter(a, StandardOpenOption.CREATE_NEW).close());
		Files.newBufferedWriter(b).append("b").close();
		HAssert.assertException(FileAlreadyExistsException.class, "\"b\" already exists!", () -> Files.newBufferedWriter(b, StandardOpenOption.CREATE_NEW).close());
	}

	@Test
	public void fileCreationTime() throws IOException {
		final FileTime expected = FileTimeMatcher.now();
		HConcurrent.wait(100);
		final Path path = createPath("a.txt");
		try (final FileTimeTester a = FileTimeTester.modify(path)) {
			Files.newByteChannel(path, HCollection.asSet(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE), HBasicFileAttributes.createCreationTime(expected)).close();
		}

		final BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
		Assert.assertEquals(expected, attributes.creationTime());
		Assert.assertTrue(attributes.lastAccessTime().compareTo(expected) > 0);
	}

	@Test
	public void fileLoopback() throws IOException {
		final Path path = createPath("a.txt");
		final String content = "Hello, World!";

		try (final FileTimeTester tester = FileTimeTester.all(path)) {
			Files.newBufferedWriter(path).append(content).append(System.lineSeparator()).close();
		}

		try (final FileTimeTester tester = FileTimeTester.read(path)) {
			try (final BufferedReader reader = Files.newBufferedReader(path)) {
				Assert.assertEquals(content, reader.readLine());
			}
		}
	}

	@Test
	public void fileOptionAppend() throws IOException {
		final Path path = createPath("a.txt");
		Files.newBufferedWriter(path).append("Hello, World!").append(System.lineSeparator()).close();
		try (final SeekableByteChannel channel = Files.newByteChannel(path, StandardOpenOption.READ, StandardOpenOption.APPEND)) {
			HAssert.assertEquals(0, channel.position());
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

		try (final FileTimeTester tester = FileTimeTester.read(path)) {
			try (final SeekableByteChannel channel = Files.newByteChannel(path)) {
				channel.position(7);
				try (final BufferedReader reader = new BufferedReader(new InputStreamReader(Channels.newInputStream(channel)))) {
					Assert.assertEquals("World!", reader.readLine());
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
			Assert.assertArrayEquals(new byte[] { 1, 2, 0, 0, 0, 3, 4 }, buffer.array());
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
			Assert.assertEquals(content, reader.readLine());
		}
		HAssert.assertThat(Files.readAttributes(b, BasicFileAttributes.class), new FieldMatcher<>(attributes, basicFileAttributeFunctions));
	}

	@Test
	public void moveExistingFail() throws IOException {
		final Path a = createPath("a"), b = createPath("b");
		Files.createDirectory(a);
		Files.newBufferedWriter(b).append("Hello, World!").append(System.lineSeparator()).close();
		HAssert.assertException(FileAlreadyExistsException.class, "\"b\" already exists!", () -> Files.move(a, b));
		Assert.assertTrue(Files.isRegularFile(b));
	}

	@Test
	public void moveExistingReplace() throws IOException {
		final Path a = createPath("a"), b = createPath("b");
		Files.createDirectory(a);
		Files.newBufferedWriter(b).append("Hello, World!").append(System.lineSeparator()).close();
		Files.move(a, b, StandardCopyOption.REPLACE_EXISTING);
		Assert.assertTrue(Files.isDirectory(b));
	}

	@Test
	public void moveNonAtomic() throws IOException {
		final Path a = createPath("a"), b = createPath("b");
		final String content = "Hello, World!";
		Files.newBufferedWriter(a).append(content).append(System.lineSeparator()).close();
		final BasicFileAttributes attributes = Files.readAttributes(a, BasicFileAttributes.class);

		Files.move(a, b);

		try (final BufferedReader reader = Files.newBufferedReader(b)) {
			Assert.assertEquals(content, reader.readLine());
		}
		HAssert.assertThat(Files.readAttributes(b, BasicFileAttributes.class), new FieldMatcher<>(attributes, basicFileAttributeFunctions));
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
}
