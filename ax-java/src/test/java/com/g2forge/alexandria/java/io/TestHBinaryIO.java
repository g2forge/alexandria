package com.g2forge.alexandria.java.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class TestHBinaryIO {
	@Test
	public void copy() throws IOException {
		final String string = "Hello, World!";
		try (final ByteArrayInputStream input = new ByteArrayInputStream(string.getBytes()); final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			HBinaryIO.copy(input, output);
			Assert.assertEquals(string, new String(output.toByteArray()));
		}
	}
}
