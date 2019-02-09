package com.g2forge.alexandria.filesystem.attributes;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class FileAttributeName {
	protected final String attributes;
	
	protected final String attribute;
	
	public FileAttributeName(String string) {
		final int colon = string.indexOf(':');
		if (colon >= 0) {
			attributes = string.substring(0, colon);
			attribute = string.substring(colon + 1, string.length());
		} else {
			attributes = HBasicFileAttributes.ATTRIBUTES_NAME;
			attribute = string;
		}
	}
	
	@Override
	public String toString() {
		return getAttributes() + ":" + getAttribute();
	}

	public static String createName(String attributes, String attribute) {
		return attributes + ":" + attribute;
	}
}
