package com.g2forge.alexandria.filesystem.memory;

import java.net.URI;
import java.net.URISyntaxException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor class MemoryURI {
	protected final String scheme;

	protected final String key;

	protected final String path;

	public MemoryURI(String scheme, URI uri) {
		this.scheme = scheme;
		try {
			// Check the scheme
			final String _scheme = uri.getScheme();
			if ((_scheme == null) || !_scheme.equalsIgnoreCase(getScheme())) throw new IllegalArgumentException("URI scheme must be '" + getScheme() + "'");

			// Extract the filesystem name
			final String raw = uri.getRawSchemeSpecificPart();
			final int separator = raw.indexOf('!');
			this.key = (separator != -1) ? raw.substring(0, separator) : raw;
			this.path = (separator != -1) ? raw.substring(separator + 1) : null;
		} catch (Throwable exception) {
			throw new IllegalArgumentException("Could not extract memory file system key from URI \"" + uri + "\"!", exception);
		}
	}

	public URI toURI() throws URISyntaxException {
		return new URI(getScheme(), getKey() + "!" + getPath(), null);
	}
}