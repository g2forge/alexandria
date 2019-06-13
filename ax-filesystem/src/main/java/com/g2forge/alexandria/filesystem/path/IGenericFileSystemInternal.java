package com.g2forge.alexandria.filesystem.path;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

/**
 * When implementing a custom {@link FileSystem} using {@link GenericPath} you will need to use this interface to allow {@link GenericPath} to access certain
 * internal methods of your file system. Your concrete implementation of this interface may also store per-path information, such as connections to remote
 * servers or a path to the local zip file that contains the path.
 * 
 * @param <P> The type of paths supported by your file system.
 */
public interface IGenericFileSystemInternal<P> {
	/**
	 * Get the {@link FileSystem} which may not be the same object as this one, if this object is used to store per-path information.
	 * 
	 * @return The {@link FileSystem}.
	 */
	public FileSystem asFileSystem();

	/**
	 * The appropriate string to represent the parent of a directory, generally "..".
	 * 
	 * @return A string representing the parent directory in a relative path.
	 */
	public default String getParent() {
		return "..";
	}

	/**
	 * Get the file system provider.
	 * 
	 * @return The file system provider.
	 */
	public IGenericFileSystemProviderInternal<P> getProvider();

	/**
	 * The appropriate string to represent the same directory, generally ".".
	 * 
	 * @return A string representing the same directory in a relative path.
	 */
	public default String getSelf() {
		return ".";
	}

	/**
	 * Register to receive watch events in exactly the manner prescribed by
	 * {@link java.nio.file.Path#register(WatchService, java.nio.file.WatchEvent.Kind[], java.nio.file.WatchEvent.Modifier...)}. This is abstracted from the
	 * path, because it will generally by implemented by the filesystem or perhaps the provider.
	 * 
	 * @param path the path to watch
	 * @param watcher the watch service to which the path is to be registered
	 * @param events the events for which the path should be registered
	 * @param modifiers the modifiers, if any, that modify how the path is registered
	 *
	 * @return a key representing the registration of the path with the given watch service
	 * @throws IOException if an I/O exception occurs
	 */
	public WatchKey register(P path, WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException;
}
