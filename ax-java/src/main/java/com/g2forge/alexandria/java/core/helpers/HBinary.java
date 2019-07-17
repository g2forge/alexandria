package com.g2forge.alexandria.java.core.helpers;

import com.g2forge.alexandria.java.core.marker.Helpers;
import com.g2forge.alexandria.java.core.math.HMath;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HBinary {
	protected static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	public static String toHex(byte... bytes) {
		final char[] retVal = new char[bytes.length * 2];
		for (int i = 0; i < bytes.length; i++) {
			final int value = bytes[i];
			final int offset = i * 2;
			retVal[offset] = HEX_ARRAY[(value >>> 4) & 0x0F];
			retVal[offset + 1] = HEX_ARRAY[value & 0x0F];
		}
		return new String(retVal);
	}

	public static byte[] fromHex(String string) {
		final byte[] retVal = new byte[HMath.divideCeiling(string.length(), 2)];
		for (int i = 0; i < string.length(); i++) {
			final char c = string.charAt(i);
			final byte v;
			if ((c >= '0') && (c <= '9')) v = (byte) (c - '0');
			else if ((c >= 'a') && (c <= 'f')) v = (byte) ((c - 'a') + 10);
			else if ((c >= 'A') && (c <= 'F')) v = (byte) ((c - 'A') + 10);
			else throw new IllegalArgumentException(String.format("Character '%1$c' at index %2$d is not hexadecimal (string is \"%3$s\")", c, i, string));
			retVal[i >>> 1] |= v << ((~i & 0x1) << 2);
		}
		return retVal;
	}

	public static byte[] toBytes(long... longs) {
		final int conversion = Long.SIZE / Byte.SIZE, c1 = conversion - 1;
		final byte[] retVal = new byte[longs.length * conversion];
		for (int i = 0; i < retVal.length; i++) {
			retVal[i] = (byte) (longs[i / conversion] >>> ((c1 - (i & c1)) * Byte.SIZE));
		}
		return retVal;
	}

	public static long[] toLongs(byte... bytes) {
		final int conversion = Long.SIZE / Byte.SIZE, c1 = conversion - 1;
		final long[] retVal = new long[HMath.divideCeiling(bytes.length, conversion)];
		for (int i = 0; i < bytes.length; i++) {
			retVal[i / conversion] |= bytes[i] << ((c1 - (i & c1)) * Byte.SIZE);
		}
		return retVal;
	}
}
