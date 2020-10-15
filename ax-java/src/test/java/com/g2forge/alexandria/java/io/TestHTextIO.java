package com.g2forge.alexandria.java.io;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.resource.IResource;
import com.g2forge.alexandria.java.core.resource.Resource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class TestHTextIO {
	@Getter
	@RequiredArgsConstructor
	protected static class LimitedInputStream extends InputStream {
		protected final InputStream stream;

		protected final int limit;

		@Override
		public int read() throws IOException {
			return getStream().read();
		}

		public int read(byte b[], int off, int len) throws IOException {
			return getStream().read(b, off, Math.min(len, getLimit()));
		}
	}

	@Test
	public void isEqual() {
		final IResource resource = new Resource(getClass(), "a.txt");
		Assert.assertTrue(HTextIO.isEqual(resource.getResourceAsStream(true), resource.getResourceAsStream(true)));
	}

	@Test
	public void limited() {
		final IResource resource = new Resource(getClass(), "a.txt");
		Assert.assertEquals(HTextIO.readAll(resource.getResourceAsStream(true), true), HTextIO.readAll(new LimitedInputStream(resource.getResourceAsStream(true), 4), true));
	}

	@Test
	public void notEqual() {
		Assert.assertFalse(HTextIO.isEqual(new Resource(getClass(), "a.txt").getResourceAsStream(true), new Resource(getClass(), "b.txt").getResourceAsStream(true)));
	}
}
