package com.g2forge.alexandria.media;

public interface IMediaType {
	public IFileExtensions getFileExtensions();

	public default MimeType getMimeType() {
		return null;
	}

	/**
	 * Is this media type text, or binary?
	 * 
	 * @return {@code true} if this media type is textual, {@code false} if it is binary.
	 */
	public boolean isText();

	/**
	 * Return a human readable representation of this media type.
	 * 
	 * @return A human readable representation of this media type.
	 */
	public String toString();
}
