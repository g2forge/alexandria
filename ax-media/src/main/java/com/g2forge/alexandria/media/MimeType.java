package com.g2forge.alexandria.media;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class MimeType {
	protected final String type;

	protected final String subtype;

	@Override
	public String toString() {
		return getType() + "/" + getSubtype();
	}
}
