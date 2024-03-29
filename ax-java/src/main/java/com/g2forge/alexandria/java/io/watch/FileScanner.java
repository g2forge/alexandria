package com.g2forge.alexandria.java.io.watch;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.concurrent.AThreadActor;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.IConsumer1;
import com.g2forge.alexandria.java.io.RuntimeIOException;
import com.g2forge.alexandria.java.io.watch.FileWatcher.IWatchHandler;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
public class FileScanner extends AThreadActor {
	@RequiredArgsConstructor
	@Getter
	@ToString
	@EqualsAndHashCode
	public static class Event {
		protected final Set<Path> paths;

		/** {@code true} if this event is the result of a scan (as opposed to a change). */
		protected final boolean scan;
	}

	protected final IConsumer1<Event> handler;

	protected final IConsumer1<Throwable> errorHandler;

	protected final boolean reportOnScan;

	protected final boolean reportUnsupportedWatch;

	protected final Set<Path> directories;

	protected final LinkedHashSet<Event> queue = new LinkedHashSet<>();

	/** A map of the directories we have scanned, so that we can watch recursively, to their watcher controls. */
	protected final Map<Path, ICloseable> scanned = new HashMap<>();

	public FileScanner(IConsumer1<Event> handler, IConsumer1<Throwable> errorHandler, boolean reportOnScan, boolean reportUnsupportedWatch, Path... directories) {
		this(handler, errorHandler, reportOnScan, reportUnsupportedWatch, HCollection.asSet(directories));
	}

	protected void handle(FileWatcher watcher) {
		final List<Event> events;
		// Grab the next changed file at the source that we should handle
		synchronized (queue) {
			if (queue.isEmpty()) try {
				queue.wait();
			} catch (InterruptedException exception) {
				return;
			}

			events = new ArrayList<>(queue);
			queue.clear();
			if (events.isEmpty()) return;
		}

		{ // Scan anything we found
			final LinkedHashSet<Path> allPaths = events.stream().map(Event::getPaths).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
			boolean foundNew = false;
			for (Path path : allPaths) {
				if (Files.isDirectory(path)) {
					try {
						foundNew |= scan(path, watcher);
					} catch (RuntimeIOException | UncheckedIOException exception) {
						if (!(exception.getCause() instanceof NoSuchFileException) || Files.isDirectory(path)) errorHandler.accept(exception);
						foundNew = true;
					} catch (Throwable throwable) {
						errorHandler.accept(throwable);
						foundNew = true;
					}
				}
			}
			if (foundNew) {
				synchronized (queue) {
					final List<Event> events2 = new ArrayList<>(queue);
					queue.clear();
					queue.addAll(events);
					queue.addAll(events2);
				}
				return;
			}
		}

		// Handle any changes/events
		for (Event event : simplify(events)) {
			if (!reportOnScan && event.isScan()) continue;
			if (event.getPaths().isEmpty()) continue;
			try {
				handler.accept(event);
			} catch (Throwable throwable) {
				errorHandler.accept(throwable);
			}
		}
	}

	@Override
	public FileScanner open() {
		return (FileScanner) super.open();
	}

	@Override
	protected void run() {
		// A watcher to help us watch for filesystem changes
		try (final FileWatcher watcher = new FileWatcher()) {
			watcher.open();
			for (Path directory : directories) {
				queue.add(new Event(HCollection.asSet(directory), true));
				handle(watcher);

			}

			while (isOpen()) {
				handle(watcher);
			}
		}
	}

	protected boolean scan(Path directory, FileWatcher watcher) {
		UnsupportedOperationException unsupported = null;

		synchronized (scanned) {
			if (scanned.containsKey(directory)) return false;

			final IWatchHandler handler = (event, path) -> {
				synchronized (queue) {
					// Stop watching a directory if it's deleted
					if (scanned.containsKey(path) && !Files.isDirectory(path)) {
						scanned.remove(path).close();
					}
					queue.add(new Event(HCollection.asSet(path), false));
					queue.notifyAll();
				}
			};

			try {
				// Watch the directory
				final ICloseable watch = watcher.watch(directory, handler, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
				// Record that we've scanned the directory
				scanned.put(directory, watch);
			} catch (UnsupportedOperationException exception) {
				// We can't watch this, but we might want to scan it anyway
				unsupported = exception;
				// Record that we've scanned the directory
				scanned.put(directory, () -> {});
			}
		}

		// If we were able to watch, or if the caller wanted files reported during the scan...
		if ((unsupported == null) || reportOnScan) {
			// Scan the directory so that we don't miss any changes from before we knew there was directory here
			synchronized (queue) {
				try {
					final Set<Path> children = Stream.concat(Stream.of(directory), Files.list(directory)).collect(Collectors.toCollection(LinkedHashSet::new));
					queue.add(new Event(children, true));
					queue.notifyAll();
				} catch (IOException exception) {
					throw new RuntimeIOException(exception);
				}
			}
		}

		if ((unsupported != null) && reportUnsupportedWatch) throw new UnsupportedOperationException("Cannot watch \"" + directory + "\"", unsupported);
		return true;
	}

	protected List<Event> simplify(List<Event> events) {
		if ((events == null) || (events.size() < 2)) return events;

		final List<Event> retVal = new ArrayList<>();
		Set<Path> paths = null;
		boolean scan = false;
		boolean pathsCopied = false;

		for (Event event : events) {
			if (paths == null) {
				paths = event.getPaths();
				scan = event.isScan();
				continue;
			}

			if (scan == event.isScan()) {
				if (!pathsCopied) {
					paths = new LinkedHashSet<>(paths);
					pathsCopied = true;
				}
				paths.addAll(event.getPaths());
			} else {
				retVal.add(new Event(Collections.unmodifiableSet(paths), scan));
				paths = event.getPaths();
				scan = event.isScan();
				pathsCopied = false;
			}
		}
		if (paths != null) retVal.add(new Event(Collections.unmodifiableSet(paths), scan));
		return retVal;
	}
}
