package com.g2forge.alexandria.filesystem.path;

import java.net.URI;
import java.net.URISyntaxException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FileSystemPathURI {
	protected final String scheme;

	protected final String fileSystem;

	protected final String path;

	public FileSystemPathURI(String scheme, URI uri) {
		this.scheme = scheme;
		try {
			// Check the scheme
			final String _scheme = uri.getScheme();
			if ((_scheme == null) || !_scheme.equalsIgnoreCase(getScheme())) throw new IllegalArgumentException("URI scheme must be '" + getScheme() + "'");

			// Extract the filesystem name
			final String raw = uri.getRawSchemeSpecificPart();
			final int separator = raw.indexOf('!');
			this.fileSystem = (separator != -1) ? raw.substring(0, separator) : raw;
			this.path = (separator != -1) ? raw.substring(separator + 1) : null;
		} catch (Throwable exception) {
			throw new IllegalArgumentException("Could not extract file system name from URI \"" + uri + "\"!", exception);
		}
	}

	public URI toURI() throws URISyntaxException {
		return new URI(getScheme(), getFileSystem() + "!" + getPath(), null);
	}
}