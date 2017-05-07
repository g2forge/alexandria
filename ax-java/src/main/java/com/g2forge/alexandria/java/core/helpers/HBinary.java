package com.g2forge.alexandria.java.core.helpers;

import com.g2forge.alexandria.java.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HBinary {
	protected static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	public static String toHex(byte[] bytes) {
		final char[] retVal = new char[bytes.length * 2];
		for (int i = 0; i < bytes.length; i++) {
			final int value = bytes[i];
			final int offset = i * 2;
			retVal[offset] = HEX_ARRAY[(value >>> 4) & 0x0F];
			retVal[offset + 1] = HEX_ARRAY[value & 0x0F];
		}
		return new String(retVal);
	}
}
