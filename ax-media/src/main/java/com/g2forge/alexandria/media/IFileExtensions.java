package com.g2forge.alexandria.media;

public interface IFileExtensions {
	public String getDefaultExtension();

	public boolean isMatch(String extension);
}
