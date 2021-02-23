package com.g2forge.alexandria.java.io.dataaccess;

import java.io.ByteArrayInputStream;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ByteArrayDataSource extends AInputStreamDataSource {
	protected final ByteArrayInputStream stream;

	public ByteArrayDataSource(byte... buffer) {
		this(new ByteArrayInputStream(buffer));
	}

	public ByteArrayDataSource(byte[] buffer, int offset, int length) {
		this(new ByteArrayInputStream(buffer, offset, length));
	}
}