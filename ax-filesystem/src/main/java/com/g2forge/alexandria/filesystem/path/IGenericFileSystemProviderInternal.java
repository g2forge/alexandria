package com.g2forge.alexandria.filesystem.path;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;

import com.g2forge.alexandria.java.close.ICloseable;

/**
 * When implementing a custom {@link FileSystemProvider} using {@link GenericFileSystem} you will need to use this interface to allow {@link GenericFileSystem}
 * to access certain internal methods of your file system provider. Your concrete implementation of this interface may also store per-filesystem information,
 * such as connections to remote servers or a path to the local zip file that contains the filesystem.
 * 
 * @param <P> The type of paths supported by your file system provider.
 */
public interface IGenericFileSystemProviderInternal<P> extends ICloseable {
	/**
	 * Convert the specified path to a URI. Used to implement {@link GenericPath#toUri()}. This must be delegated to the file system provider, which is often
	 * the only class that knows the URI scheme, and other critical information.
	 * 
	 * @param path The path to convert to a URI.
	 * @return A URI representing the specified path.
	 * @throws URISyntaxException If the URI cannot be created.
	 */
	public URI toURI(P path) throws URISyntaxException;

	/**
	 * Get the {@link FileSystemProvider} which may not be the same object as this one, if this object is used to store per-filesystem information.
	 * 
	 * @return The {@link FileSystemProvider}.
	 */
	public FileSystemProvider asFileSystemProvider();

	/**
	 * Get the names of supported file attribute views. This is used to implement {@link GenericFileSystem#supportedFileAttributeViews()} and is generally
	 * delegated to {@link com.g2forge.alexandria.filesystem.attributes.accessor.AttributeViewAccessorRegistry#getAttributesNames()}.
	 * 
	 * @return The names of the file attribute views (not the attributes themselves) supported by this file system provider, optionally specialized to this file
	 *         system.
	 */
	public Set<String> getSupportedFileAttributeViews();
}
