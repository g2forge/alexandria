package com.g2forge.alexandria.java.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.g2forge.alexandria.java.function.IThrowFunction1;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum HashAlgorithm {
	SHA_1,
	SHA_256;

	public String getAlgorithmName() {
		return name().replace('_', '-');
	}

	public byte[] hash(String value) {
		return hash(value, (Charset) null);
	}

	public byte[] hash(String value, Charset charset) {
		final byte[] bytes = charset == null ? value.getBytes() : value.getBytes(charset);
		return hash(bytes, ByteArrayInputStream::new);
	}

	public <T> byte[] hash(T value, IThrowFunction1<T, InputStream, IOException> open) {
		final MessageDigest digest;
		try {
			digest = MessageDigest.getInstance(getAlgorithmName());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		try (final InputStream stream = open.apply(value)) {
			final byte[] buffer = new byte[HIO.getRecommendedBufferSize()];
			int n = 0;
			while (n != -1) {
				n = stream.read(buffer);
				if (n > 0) {
					digest.update(buffer, 0, n);
				}
			}
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
		return digest.digest();
	}
}
