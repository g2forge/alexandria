package com.g2forge.alexandria.java.core.helpers;

import org.junit.Assert;
import org.junit.Test;

public class TestHBinary {
	@Test
	public void fromHex() {
		Assert.assertArrayEquals(new byte[] { (byte) 0xA5 }, HBinary.fromHex("a5"));
		Assert.assertArrayEquals(new byte[] { (byte) 0xba, (byte) 0x5e }, HBinary.fromHex("BA5e"));
	}

	@Test
	public void toBytes() {
		Assert.assertArrayEquals(new byte[] { (byte) 0xBA, 0x5E, (byte) 0xBA, 0x11, 0x01, 0x23, 0x45, 0x67 }, HBinary.toBytes(0xBA5EBA1101234567l));
	}

	@Test
	public void toHex() {
		Assert.assertEquals("00", HBinary.toHex((byte) 0));
		Assert.assertEquals("BA5E", HBinary.toHex((byte) 0xBA, (byte) 0x5E));
	}

	@Test
	public void toLongs() {
		Assert.assertArrayEquals(new long[] { 0x0102 }, HBinary.toLongs((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0x01, (byte) 0x02));
	}
}
