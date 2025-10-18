package com.g2forge.alexandria.java.io.watch;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.concurrent.AThreadActor;
import com.g2forge.alexandria.java.io.RuntimeIOException;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class FileWatcher extends AThreadActor {
	@Getter
	@RequiredArgsConstructor
	@ToString
	protected static class FileSystemContext {
		protected final FileSystem fileSystem;

		protected final WatchService watchService;

		protected final Map<WatchKey, WatchValue> keys = new WeakHashMap<>();

		public ICloseable watch(Path path, IWatchHandler handler, WatchEvent.Kind<?>... kinds) {
			final WatchKey key;
			try {
				key = path.register(getWatchService(), kinds);
			} catch (IOException exception) {
				throw new RuntimeIOException(String.format("Failed to register to watch %1$s for %2$s!", path, Stream.of(kinds).map(WatchEvent.Kind::name).collect(Collectors.joining(", "))), exception);
			}
			synchronized (getKeys()) {
				getKeys().put(key, new WatchValue(path, handler));
			}
			return () -> key.cancel();
		}
	}

	@FunctionalInterface
	public static interface IWatchHandler {
		public void accept(WatchEvent<Path> event, Path path);
	}

	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	protected static class WatchValue {
		protected final Path path;

		protected final IWatchHandler handler;
	}

	protected final Map<FileSystem, FileSystemContext> fileSystems = new IdentityHashMap<>();
	
	protected boolean started = false;

	@Override
	public FileWatcher open() {
		return (FileWatcher) super.open();
	}

	@Override
	protected void run() {
		List<FileSystemContext> list;
		synchronized (fileSystems) {
			list = new ArrayList<>(fileSystems.values());
		}
		int i = 0;
		while (isOpen()) {
			if (list.isEmpty()) {
				synchronized (fileSystems) {
					try {
						fileSystems.wait();
					} catch (InterruptedException e) {}
					list = new ArrayList<>(fileSystems.values());
				}
			} else {
				final FileSystemContext current = list.get(i);

				// Wait for a key to be signaled
				final WatchKey key;
				try {
					key = current.getWatchService().take();
				} catch (InterruptedException exception) {
					continue;
				}

				// Get the path this key corresponds to
				final WatchValue value;
				synchronized (current.getKeys()) {
					value = current.getKeys().get(key);
				}
				if (value == null) {
					continue;
				}

				// Look through all the events on the key
				for (WatchEvent<?> event : key.pollEvents()) {
					final WatchEvent.Kind<?> kind = event.kind();
					if (StandardWatchEventKinds.OVERFLOW.equals(kind)) throw new UnsupportedOperationException(/* TODO */);
					else if (StandardWatchEventKinds.ENTRY_CREATE.equals(kind) || StandardWatchEventKinds.ENTRY_MODIFY.equals(kind) || StandardWatchEventKinds.ENTRY_DELETE.equals(kind)) {
						@SuppressWarnings("unchecked")
						final WatchEvent<Path> eventPath = (WatchEvent<Path>) event;

						// The context for directory events is the file name of the child which has changed
						final Path child = value.getPath().resolve(eventPath.context());

						// Handle the event
						value.getHandler().accept(eventPath, child);
					}

				}

				// Reset the key, and remove it if it's not accessible any more
				if (!key.reset()) {
					synchronized (current.getKeys()) {
						current.getKeys().remove(key);
						synchronized (fileSystems) {
							// If there are no more keys
							if (current.getKeys().isEmpty()) {
								fileSystems.remove(current.getFileSystem());
							}
						}
					}
				}

				synchronized (fileSystems) {
					list = new ArrayList<>(fileSystems.values());
				}
				final int found = list.indexOf(current);
				final int modulus = list.isEmpty() ? 1 : list.size();
				if (found < 0) i = i % modulus; // Don't increment, since the current file system was removed, just keep going
				else i = (i + 1) % modulus; // Move on to the next file system
			}
		}
	}

	public ICloseable watch(Path path, IWatchHandler handler, WatchEvent.Kind<?>... kinds) {
		synchronized (fileSystems) {
			final FileSystemContext context = fileSystems.computeIfAbsent(path.getFileSystem(), fs -> {
				try {
					return new FileSystemContext(fs, fs.newWatchService());
				} catch (IOException exception) {
					throw new RuntimeIOException("Failed to create watch service!", exception);
				}
			});
			final ICloseable retVal = context.watch(path, handler, kinds);
			fileSystems.notifyAll();
			return retVal;
		}
	}
}
