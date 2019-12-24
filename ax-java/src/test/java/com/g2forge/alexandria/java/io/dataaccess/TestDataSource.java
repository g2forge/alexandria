package com.g2forge.alexandria.java.io.dataaccess;

import java.io.ByteArrayInputStream;

import org.junit.Assert;
import org.junit.Test;

public class TestDataSource {
	@Test
	public void bytes() {
		final byte[] bytes = { 0x00, 0x01, 0x02 };
		final InputStreamDataSource source = new InputStreamDataSource(new ByteArrayInputStream(bytes));
		Assert.assertArrayEquals(bytes, source.getBytes());
	}

	@Test
	public void string() {
		final String string = "Hello, World!";
		final InputStreamDataSource source = new InputStreamDataSource(new ByteArrayInputStream(string.getBytes()));
		Assert.assertEquals(string, source.getString());
	}
}
