package com.g2forge.alexandria.java.io.watch;

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
		test(null, (temp, file) -> writeFile(file, new byte[] { 0 }, StandardOpenOption.CREATE_NEW), StandardWatchEventKinds.ENTRY_CREATE);
	}

	@Test
	public void delete() throws IOException, InterruptedException {
		test((temp, file) -> writeFile(file, new byte[] { 0 }, StandardOpenOption.CREATE_NEW), (temp, file) -> {
			try {
				Files.delete(file);
			} catch (IOException exception) {
				throw new RuntimeIOException(exception);
			}
		}, StandardWatchEventKinds.ENTRY_DELETE);
	}

	@Test
	public void modify() throws IOException, InterruptedException {
		test((temp, file) -> writeFile(file, new byte[] { 0 }, StandardOpenOption.CREATE_NEW), (temp, file) -> writeFile(file, new byte[] { 1 }, StandardOpenOption.TRUNCATE_EXISTING), StandardWatchEventKinds.ENTRY_MODIFY);
	}

	@SafeVarargs
	protected final void test(final IConsumer2<Path, Path> prep, final IConsumer2<Path, Path> modify, final Kind<Path>... kinds) throws InterruptedException {
		try (final ICloseableSupplier<Path> temp = new TempDirectory(); final FileWatcher watcher = new FileWatcher()) {
			final Path file = temp.get().resolve("file");

			// Prepare for testing
			if (prep != null) prep.accept(temp.get(), file);

			// Open the watcher
			watcher.open();
			final List<WatchEvent<Path>> events = new ArrayList<>();
			watcher.watch(temp.get(), (event, path) -> {
				synchronized (events) {
					events.add(event);
				}
			}, HCollection.asSet(kinds).toArray(new Kind[0]));
			synchronized (events) {
				events.wait(10);
			}

			// Perform the modification
			if (modify != null) modify.accept(temp.get(), file);
			synchronized (events) {
				events.wait(10);
			}

			// Look for the expected events
			final Set<SimpleWatchEvent> expected = Stream.of(kinds).map(k -> new SimpleWatchEvent(k, file.getFileName())).collect(Collectors.toSet());
			synchronized (events) {
				// Test for expected events
				events.wait(1000);

				final Set<SimpleWatchEvent> actual = events.stream().map(SimpleWatchEvent::new).collect(Collectors.toSet());
				events.clear();
				Assert.assertEquals(expected, actual);

				// Make sure no more came in
				events.wait(100);
				Assert.assertEquals(HCollection.emptyList(), events.stream().map(SimpleWatchEvent::new).collect(Collectors.toList()));
			}
		}
	}

	protected void writeFile(final Path file, byte[] bytes, OpenOption... options) {
		try (final OutputStream output = Files.newOutputStream(file, options)) {
			output.write(bytes);
		} catch (IOException exception) {
			throw new RuntimeIOException(exception);
		}
	}
}
