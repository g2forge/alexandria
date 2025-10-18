package com.g2forge.alexandria.java.io.watch;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.close.ICloseableSupplier;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.io.RuntimeIOException;
import com.g2forge.alexandria.java.io.file.TempDirectory;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public class TestFileWatcher {
	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	public static class SimpleWatchEvent {
		protected final Kind<Path> kind;

		protected final Path context;

		public SimpleWatchEvent(WatchEvent<Path> watchEvent) {
			this(watchEvent.kind(), watchEvent.context());
		}
	}

	@Test
	public void create() throws IOException, InterruptedException {
		test(null, (temp, file) -> writeFile(file, new byte[] { 0, 1, 2, 3 }, StandardOpenOption.CREATE_NEW), StandardWatchEventKinds.ENTRY_CREATE);
	}

	@Test
	public void delete() throws IOException, InterruptedException {
		test((temp, file) -> writeFile(file, new byte[] { 0, 1, 2, 3 }, StandardOpenOption.CREATE_NEW), (temp, file) -> {
			try {
				Files.delete(file);
			} catch (IOException exception) {
				throw new RuntimeIOException(exception);
			}
		}, StandardWatchEventKinds.ENTRY_DELETE);
	}

	@Test
	public void modify() throws IOException, InterruptedException {
		test((temp, file) -> writeFile(file, new byte[] { 0, 1, 2, 3 }, StandardOpenOption.CREATE_NEW), (temp, file) -> writeFile(file, new byte[] { 1 }, StandardOpenOption.TRUNCATE_EXISTING), StandardWatchEventKinds.ENTRY_MODIFY);
	}

	@SafeVarargs
	protected final void test(final IConsumer2<Path, Path> prep, final IConsumer2<Path, Path> modify, final Kind<Path>... kinds) throws InterruptedException {
		try (final ICloseableSupplier<Path> temp = new TempDirectory();
			final FileWatcher watcher = new FileWatcher()) {
			final Path file = temp.get().resolve("file");
			final Object delay = new Object();

			// Prepare for testing
			if (prep != null) prep.accept(temp.get(), file);

			// Open the watcher
			watcher.open().awaitRun();
			final List<WatchEvent<Path>> events = new ArrayList<>();
			watcher.watch(temp.get(), (event, path) -> {
				synchronized (events) {
					events.add(event);
				}
			}, HCollection.asSet(kinds).toArray(new Kind[0]));

			// Delay to make sure we're watching when things happen
			synchronized (delay) {
				delay.wait(100);
			}

			// Perform the modification
			if (modify != null) modify.accept(temp.get(), file);

			// Look for the expected events
			final Set<SimpleWatchEvent> expected = Stream.of(kinds).map(k -> new SimpleWatchEvent(k, file.getFileName())).collect(Collectors.toCollection(LinkedHashSet::new));
			final int maxIterations = 100, delayMS = 100;
			int i = 0;
			while (!expected.isEmpty() && (i < maxIterations)) {
				final Set<SimpleWatchEvent> actual;
				synchronized (events) {
					actual = events.stream().map(SimpleWatchEvent::new).collect(Collectors.toCollection(LinkedHashSet::new));
					events.clear();
				}
				// Mark everything we found
				expected.removeAll(actual);

				// Wait a little while before we bother checking again
				synchronized (delay) {
					delay.wait(delayMS);
				}

				i++;
			}

			// Make sure no more came in
			Assert.assertEquals("Even after " + maxIterations * delayMS + "ms, we didn't recive all the expected events", new LinkedHashSet<>(), expected);
		}
	}

	protected void writeFile(final Path file, byte[] bytes, OpenOption... options) {
		try (final OutputStream output = Files.newOutputStream(file, options)) {
			output.write(bytes);
			output.flush();

			if (output instanceof FileOutputStream) {
				@SuppressWarnings("resource")
				final FileOutputStream fileOutputStream = (FileOutputStream) output;
				fileOutputStream.getChannel().force(true);
				fileOutputStream.getFD().sync();
			}
		} catch (IOException exception) {
			throw new RuntimeIOException(exception);
		}
	}
}
