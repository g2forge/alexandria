package com.g2forge.alexandria.java.io.dataaccess;

import org.junit.Assert;
import org.junit.Test;

public class TestDataSink {
	@Test
	public void bytes() {
		final ByteArrayDataSink sink = new ByteArrayDataSink();
		final byte[] bytes = { 0x00, 0x01, 0x02 };
		sink.put(bytes);
		Assert.assertArrayEquals(bytes, sink.getStream().toByteArray());
	}

	@Test
	public void string() {
		final ByteArrayDataSink sink = new ByteArrayDataSink();
		final String string = "Hello, World!";
		sink.put(string);
		Assert.assertEquals(string, sink.getStream().toString());
	}
}
