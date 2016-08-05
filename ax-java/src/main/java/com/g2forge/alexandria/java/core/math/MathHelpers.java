package com.g2forge.alexandria.java.core.math;

import com.g2forge.alexandria.java.enums.EnumException;

public abstract class MathHelpers {
	public static byte abs(byte value) {
		if (value == Byte.MIN_VALUE) throw new UnrepresentablePrimitiveException("Twos complement integers are not symmetric");
		return (value < 0) ? (byte) -value : value;
	}

	public static double abs(double value) {
		return Math.abs(value);
	}

	public static float abs(float value) {
		return Math.abs(value);
	}

	public static int abs(int value) {
		if (value == Integer.MIN_VALUE) throw new UnrepresentablePrimitiveException("Twos complement integers are not symmetric");
		return Math.abs(value);
	}

	public static long abs(long value) {
		if (value == Long.MIN_VALUE) throw new UnrepresentablePrimitiveException("Twos complement integers are not symmetric");
		return Math.abs(value);
	}

	public static short abs(short value) {
		if (value == Short.MIN_VALUE) throw new UnrepresentablePrimitiveException("Twos complement integers are not symmetric");
		return (value < 0) ? (short) -value : value;
	}

	public static double ceil(double value) {
		return Math.ceil(value);
	}

	public static int divideCeiling(int num, int denom) {
		int retVal = num / denom;
		if (num % denom != 0) retVal++;
		return retVal;
	}

	public static long divideCeiling(long num, long denom) {
		long retVal = num / denom;
		if (num % denom != 0) retVal++;
		return retVal;
	}

	public static int divideUnsigned(int num, int denom) {
		// Test if we've got a very large (negative) denominator
		if (denom < 0) {
			// If the numerator is small (positive) then the result must be 0
			if (num >= 0) return 0;
			// If the numerator is larger (larger) then the denominator then the result is 1
			return (num >= denom) ? 1 : 0;
		} else if (num < 0) {
			if (denom == 1) return num;
			if (denom == 0) throw new ArithmeticException("Divide by zero");
			if (num == denom) return 1;

			final int shifted = num >>> 1;
			final int quo = shifted / denom, rem = shifted % denom;
			final int add = (((rem << 1) + (num & 0x1)) >= denom) ? 1 : 0;
			return (quo << 1) + add;
		} else return num / denom;
	}

	public static long divideUnsigned(long num, long denom) {
		// Test if we've got a very large (negative) denominator
		if (denom < 0) {
			// If the numerator is small (positive) then the result must be 0
			if (num >= 0) return 0l;
			// If the numerator is larger (larger) then the denominator then the result is 1
			return (num >= denom) ? 1l : 0l;
		} else if (num < 0) {
			if (denom == 1l) return num;
			if (denom == 0l) throw new ArithmeticException("Divide by zero");
			if (num == denom) return 1l;

			final long shifted = num >>> 1l;
			final long quo = shifted / denom, rem = shifted % denom;
			final long add = (((rem << 1l) + (num & 0x1l)) >= denom) ? 1l : 0l;
			return (quo << 1l) + add;
		} else return num / denom;
	}

	public static double floor(double value) {
		return Math.floor(value);
	}

	public static int interpolate(int arg0, int arg1, double scale) {
		return (int) ((scale * arg1) + ((1 - scale) * arg0));
	}

	public static int log2Ceiling(byte value) {
		return msbPosition(value, true);
	}

	public static int log2Ceiling(int value) {
		return msbPosition(value, true);
	}

	public static int log2Ceiling(long value) {
		return msbPosition(value, true);
	}

	public static int log2Ceiling(short value) {
		return msbPosition(value, true);
	}

	public static byte lsbMask(byte value, boolean bit) {
		final int mask = (1 << Byte.SIZE) - 1;
		final int _value = value & mask;
		return (byte) Integer.lowestOneBit(bit ? ~_value : _value);
	}

	public static int lsbMask(int value, boolean bit) {
		return Integer.lowestOneBit(bit ? value : ~value);
	}

	public static long lsbMask(long value, boolean bit) {
		return Long.lowestOneBit(bit ? value : ~value);
	}

	public static short lsbMask(short value, boolean bit) {
		final int mask = (1 << Short.SIZE) - 1;
		final int _value = value & mask;
		return (short) Integer.lowestOneBit(bit ? ~_value : _value);
	}

	public static int lsbPosition(byte value, boolean bit) {
		int temp = Integer.numberOfTrailingZeros((bit ? value : ~value) & ((1 << Byte.SIZE) - 1));
		if (temp >= Byte.SIZE) temp -= (Integer.SIZE - Byte.SIZE);
		return temp;
	}

	public static int lsbPosition(int value, boolean bit) {
		return Integer.numberOfTrailingZeros(bit ? value : ~value);
	}

	public static int lsbPosition(long value, boolean bit) {
		return Long.numberOfTrailingZeros(bit ? value : ~value);
	}

	public static int lsbPosition(short value, boolean bit) {
		int temp = Integer.numberOfTrailingZeros((bit ? value : ~value) & ((1 << Short.SIZE) - 1));
		if (temp >= Short.SIZE) temp -= (Integer.SIZE - Short.SIZE);
		return temp;
	}

	public static byte max(byte value0, byte value1) {
		return (byte) Math.max(value0, value1);
	}

	public static double max(double value0, double value1) {
		return Math.max(value0, value1);
	}

	public static float max(float value0, float value1) {
		return Math.max(value0, value1);
	}

	public static int max(int value0, int value1) {
		return Math.max(value0, value1);
	}

	public static long max(long value0, long value1) {
		return Math.max(value0, value1);
	}

	public static short max(short value0, short value1) {
		return (short) Math.max(value0, value1);
	}

	public static byte min(byte value0, byte value1) {
		return (byte) Math.min(value0, value1);
	}

	public static double min(double value0, double value1) {
		return Math.min(value0, value1);
	}

	public static float min(float value0, float value1) {
		return Math.min(value0, value1);
	}

	public static int min(int value0, int value1) {
		return Math.min(value0, value1);
	}

	public static long min(long value0, long value1) {
		return Math.min(value0, value1);
	}

	public static short min(short value0, short value1) {
		return (short) Math.min(value0, value1);
	}

	public static byte minmaxDelta(boolean max, byte base, byte value0, byte value1) {
		final byte delta0 = (byte) (value0 - base), delta1 = (byte) (value1 - base);
		return (max ? (delta0 >= delta1) : (delta0 <= delta1)) ? value0 : value1;
	}

	public static <T> T minmaxDelta(boolean max, byte base, byte value0, byte value1, T retVal0, T retVal1) {
		final byte delta0 = (byte) (value0 - base), delta1 = (byte) (value1 - base);
		return (max ? (delta0 >= delta1) : (delta0 <= delta1)) ? retVal0 : retVal1;
	}

	public static char minmaxDelta(boolean max, char base, char value0, char value1) {
		final char delta0 = (char) (value0 - base), delta1 = (char) (value1 - base);
		return (max ? (delta0 >= delta1) : (delta0 <= delta1)) ? value0 : value1;
	}

	public static <T> T minmaxDelta(boolean max, char base, char value0, char value1, T retVal0, T retVal1) {
		final char delta0 = (char) (value0 - base), delta1 = (char) (value1 - base);
		return (max ? (delta0 >= delta1) : (delta0 <= delta1)) ? retVal0 : retVal1;
	}

	public static double minmaxDelta(boolean max, double base, double value0, double value1) {
		final double delta0 = value0 - base, delta1 = value1 - base;
		return (max ? (delta0 >= delta1) : (delta0 <= delta1)) ? value0 : value1;
	}

	public static <T> T minmaxDelta(boolean max, double base, double value0, double value1, T retVal0, T retVal1) {
		final double delta0 = value0 - base, delta1 = value1 - base;
		return (max ? (delta0 >= delta1) : (delta0 <= delta1)) ? retVal0 : retVal1;
	}

	public static float minmaxDelta(boolean max, float base, float value0, float value1) {
		final float delta0 = value0 - base, delta1 = value1 - base;
		return (max ? (delta0 >= delta1) : (delta0 <= delta1)) ? value0 : value1;
	}

	public static <T> T minmaxDelta(boolean max, float base, float value0, float value1, T retVal0, T retVal1) {
		final float delta0 = value0 - base, delta1 = value1 - base;
		return (max ? (delta0 >= delta1) : (delta0 <= delta1)) ? retVal0 : retVal1;
	}

	public static int minmaxDelta(boolean max, int base, int value0, int value1) {
		final int delta0 = value0 - base, delta1 = value1 - base;
		return (max ? (delta0 >= delta1) : (delta0 <= delta1)) ? value0 : value1;
	}

	public static <T> T minmaxDelta(boolean max, int base, int value0, int value1, T retVal0, T retVal1) {
		final int delta0 = value0 - base, delta1 = value1 - base;
		return (max ? (delta0 >= delta1) : (delta0 <= delta1)) ? retVal0 : retVal1;
	}

	public static long minmaxDelta(boolean max, long base, long value0, long value1) {
		final long delta0 = value0 - base, delta1 = value1 - base;
		return (max ? (delta0 >= delta1) : (delta0 <= delta1)) ? value0 : value1;
	}

	public static <T> T minmaxDelta(boolean max, long base, long value0, long value1, T retVal0, T retVal1) {
		final long delta0 = value0 - base, delta1 = value1 - base;
		return (max ? (delta0 >= delta1) : (delta0 <= delta1)) ? retVal0 : retVal1;
	}

	public static short minmaxDelta(boolean max, short base, short value0, short value1) {
		final short delta0 = (short) (value0 - base), delta1 = (short) (value1 - base);
		return (max ? (delta0 >= delta1) : (delta0 <= delta1)) ? value0 : value1;
	}

	public static <T> T minmaxDelta(boolean max, short base, short value0, short value1, T retVal0, T retVal1) {
		final short delta0 = (short) (value0 - base), delta1 = (short) (value1 - base);
		return (max ? (delta0 >= delta1) : (delta0 <= delta1)) ? retVal0 : retVal1;
	}

	public static byte mod(byte n, byte m) {
		byte retVal = (byte) (n % m);
		while (retVal < 0)
			retVal += m;
		return retVal;
	}

	public static int mod(int n, int m) {
		int retVal = n % m;
		while (retVal < 0)
			retVal += m;
		return retVal;
	}

	public static long mod(long n, long m) {
		long retVal = n % m;
		while (retVal < 0)
			retVal += m;
		return retVal;
	}

	public static short mod(short n, short m) {
		short retVal = (short) (n % m);
		while (retVal < 0)
			retVal += m;
		return retVal;
	}

	public static byte msbMask(byte value, boolean bit) {
		final int mask = (1 << Byte.SIZE) - 1;
		final int _value = value & mask;
		return (byte) Integer.highestOneBit(bit ? _value : ~_value);
	}

	public static int msbMask(int value, boolean bit) {
		return Integer.highestOneBit(bit ? value : ~value);
	}

	public static long msbMask(long value, boolean bit) {
		return Long.highestOneBit(bit ? value : ~value);
	}

	public static short msbMask(short value, boolean bit) {
		final int mask = (1 << Short.SIZE) - 1;
		final int _value = value & mask;
		return (short) Integer.highestOneBit(bit ? _value : ~_value);
	}

	public static int msbPosition(byte value, boolean bit) {
		int temp = Integer.numberOfLeadingZeros((bit ? value : ~value) & ((1 << Byte.SIZE) - 1));
		if (temp >= Byte.SIZE) temp -= (Integer.SIZE - Byte.SIZE);
		return Byte.SIZE - temp - 1;

	}

	public static int msbPosition(int value, boolean bit) {
		return Integer.SIZE - Integer.numberOfLeadingZeros(bit ? value : ~value) - 1;
	}

	public static int msbPosition(long value, boolean bit) {
		return Long.SIZE - Long.numberOfLeadingZeros(bit ? value : ~value) - 1;
	}

	public static int msbPosition(short value, boolean bit) {
		int temp = Integer.numberOfLeadingZeros((bit ? value : ~value) & ((1 << Short.SIZE) - 1));
		if (temp >= Short.SIZE) temp -= (Integer.SIZE - Short.SIZE);
		return Short.SIZE - temp - 1;
	}

	public static double parseDouble(String string) {
		int index = string.lastIndexOf('/');
		if (index >= 0) return parseDouble(string.substring(0, index)) / parseDouble(string.substring(index + 1));
		return Double.parseDouble(string);
	}

	public static float parseFloat(String string) {
		int index = string.lastIndexOf('/');
		if (index >= 0) return parseFloat(string.substring(0, index)) / parseFloat(string.substring(index + 1));
		return Float.parseFloat(string);
	}

	public static int popCount(byte _value) {
		int value = _value;
		value = (value & 0x55555555) + ((value & 0xaaaaaaaa) >>> 1);
		value = (value & 0x33333333) + ((value & 0xcccccccc) >>> 2);
		value = (value & 0x0f0f0f0f) + ((value & 0xf0f0f0f0) >>> 4);
		return value;
	}

	public static int popCount(int value) {
		value = (value & 0x55555555) + ((value & 0xaaaaaaaa) >>> 1);
		value = (value & 0x33333333) + ((value & 0xcccccccc) >>> 2);
		value = (value & 0x0f0f0f0f) + ((value & 0xf0f0f0f0) >>> 4);
		value = (value & 0x00ff00ff) + ((value & 0xff00ff00) >>> 8);
		value = (value & 0x0000ffff) + ((value & 0xffff0000) >>> 16);
		return value;
	}

	public static int popCount(long value) {
		value = (value & 0x5555555555555555l) + ((value & 0xaaaaaaaaaaaaaaaal) >>> 1);
		value = (value & 0x3333333333333333l) + ((value & 0xccccccccccccccccl) >>> 2);
		value = (value & 0x0f0f0f0f0f0f0f0fl) + ((value & 0xf0f0f0f0f0f0f0f0l) >>> 4);
		value = (value & 0x00ff00ff00ff00ffl) + ((value & 0xff00ff00ff00ff00l) >>> 8);
		value = (value & 0x0000ffff0000ffffl) + ((value & 0xffff0000ffff0000l) >>> 16);
		value = (value & 0x00000000ffffffffl) + ((value & 0xffffffff00000000l) >>> 32);
		return (int) value;
	}

	public static int popCount(short _value) {
		int value = _value;
		value = (value & 0x55555555) + ((value & 0xaaaaaaaa) >>> 1);
		value = (value & 0x33333333) + ((value & 0xcccccccc) >>> 2);
		value = (value & 0x0f0f0f0f) + ((value & 0xf0f0f0f0) >>> 4);
		value = (value & 0x00ff00ff) + ((value & 0xff00ff00) >>> 8);
		return value;
	}

	public static byte power(byte value, byte exponent) {
		return (byte) Math.pow(value, exponent);
	}

	public static double power(double value, double exponent) {
		return Math.pow(value, exponent);
	}

	public static float power(float value, float exponent) {
		return (float) Math.pow(value, exponent);
	}

	public static int power(int value, int exponent) {
		return (int) Math.pow(value, exponent);
	}

	public static long power(long value, long exponent) {
		return (long) Math.pow(value, exponent);
	}

	public static short power(short value, short exponent) {
		return (short) Math.pow(value, exponent);
	}

	public static double random() {
		return Math.random();
	}

	public static byte reverse(byte value) {
		int _value = value;
		_value = ((_value >>> 1) & 0x55) | ((_value & 0x55) << 1);
		_value = ((_value >>> 2) & 0x33) | ((_value & 0x33) << 2);
		_value = ((_value >>> 4) & 0x0f) | ((_value & 0x0f) << 4);
		return (byte) _value;
	}

	public static int reverse(int value) {
		value = ((value >>> 1) & 0x55555555) | ((value & 0x55555555) << 1);
		value = ((value >>> 2) & 0x33333333) | ((value & 0x33333333) << 2);
		value = ((value >>> 4) & 0x0f0f0f0f) | ((value & 0x0f0f0f0f) << 4);
		value = ((value >>> 8) & 0x00ff00ff) | ((value & 0x00ff00ff) << 8);
		value = ((value >>> 16) & 0x0000ffff) | ((value & 0x0000ffff) << 16);
		return value;
	}

	public static long reverse(long value) {
		value = ((value >>> 1) & 0x5555555555555555l) | ((value & 0x5555555555555555l) << 1);
		value = ((value >>> 2) & 0x3333333333333333l) | ((value & 0x3333333333333333l) << 2);
		value = ((value >>> 4) & 0x0f0f0f0f0f0f0f0fl) | ((value & 0x0f0f0f0f0f0f0f0fl) << 4);
		value = ((value >>> 8) & 0x00ff00ff00ff00ffl) | ((value & 0x00ff00ff00ff00ffl) << 8);
		value = ((value >>> 16) & 0x0000ffff0000ffffl) | ((value & 0x0000ffff0000ffffl) << 16);
		value = ((value >>> 32) & 0x00000000ffffffffl) | ((value & 0x00000000ffffffffl) << 32);
		return value;
	}

	public static short reverse(short value) {
		int _value = value;
		_value = ((_value >>> 1) & 0x5555) | ((_value & 0x5555) << 1);
		_value = ((_value >>> 2) & 0x3333) | ((_value & 0x3333) << 2);
		_value = ((_value >>> 4) & 0x0f0f) | ((_value & 0x0f0f) << 4);
		_value = ((_value >>> 8) & 0x00ff) | ((_value & 0x00ff) << 8);
		return (short) _value;
	}

	/**
	 * TODO: Javadoc Rotate left by the specified amount. Rotate right if the amount is negative.
	 * 
	 * @param value
	 * @param rotation
	 * @return
	 */
	public static byte rotate(byte value, int rotation) {
		if (rotation == 0) return value;
		if (rotation < 0) {
			rotation = MathHelpers.abs(rotation);
			rotation = rotation % Byte.SIZE;
			return (byte) ((value >>> rotation) | (value << (Byte.SIZE - rotation)));
		}
		rotation = rotation % Byte.SIZE;
		return (byte) ((value << rotation) | (value >>> (Byte.SIZE - rotation)));
	}

	/**
	 * TODO: Javadoc Rotate left by the specified amount. Rotate right if the amount is negative.
	 * 
	 * @param value
	 * @param rotation
	 * @return
	 */
	public static int rotate(int value, int rotation) {
		if (rotation == 0) return value;
		if (rotation < 0) {
			rotation = MathHelpers.abs(rotation);
			rotation = rotation % Integer.SIZE;
			return (value >>> rotation) | (value << (Integer.SIZE - rotation));
		}
		rotation = rotation % Integer.SIZE;
		return (value << rotation) | (value >>> (Integer.SIZE - rotation));
	}

	/**
	 * TODO: Javadoc Rotate left by the specified amount. Rotate right if the amount is negative.
	 * 
	 * @param value
	 * @param rotation
	 * @return
	 */
	public static long rotate(long value, int rotation) {
		if (rotation == 0) return value;
		if (rotation < 0) {
			rotation = MathHelpers.abs(rotation);
			rotation = rotation % Long.SIZE;
			return (value >>> rotation) | (value << (Long.SIZE - rotation));
		}
		rotation = rotation % Long.SIZE;
		return (value << rotation) | (value >>> (Long.SIZE - rotation));
	}

	/**
	 * TODO: Javadoc Rotate left by the specified amount. Rotate right if the amount is negative.
	 * 
	 * @param value
	 * @param rotation
	 * @return
	 */
	public static short rotate(short value, int rotation) {
		if (rotation == 0) return value;
		if (rotation < 0) {
			rotation = MathHelpers.abs(rotation);
			rotation = rotation % Short.SIZE;
			return (short) ((value >>> rotation) | (value << (Short.SIZE - rotation)));
		}
		rotation = rotation % Short.SIZE;
		return (short) ((value << rotation) | (value >>> (Short.SIZE - rotation)));
	}

	public static double round(double value) {
		return Math.round(value);
	}

	/**
	 * Return <code>-1</code>, <code>0</code> or <code>1</code> if the input is negative, zero or positive respectively.
	 * 
	 * @param value
	 *            The number to compute the signum of.
	 * @return <code>-1</code>, <code>0</code> or <code>1</code> if the input is negative, zero or positive respectively.
	 */
	public static Sign sign(byte value) {
		if (value < 0) return Sign.Negative;
		if (value > 0) return Sign.Positive;
		return Sign.Zero;
	}

	public static byte sign(byte value, byte sign, boolean delta) {
		if (sign == 0) return 0;
		if (!delta) value = abs(value);
		return (sign > 0) ? value : (byte) -value;
	}

	public static byte sign(byte value, Sign sign, boolean delta) {
		if (sign == Sign.Zero) return 0;
		if (!delta) value = abs(value);
		switch (sign) {
			case Positive:
				return value;
			case Negative:
				return (byte) -value;
			default:
				throw new EnumException(Sign.class, sign);
		}
	}

	/**
	 * Return <code>-1</code>, <code>0</code> or <code>1</code> if the input is negative, zero or positive respectively.
	 * 
	 * @param value
	 *            The number to compute the signum of.
	 * @return <code>-1</code>, <code>0</code> or <code>1</code> if the input is negative, zero or positive respectively.
	 */
	public static Sign sign(double value) {
		if (Double.isNaN(value)) return null;
		return sign((int) Math.signum(value));
	}

	public static double sign(double value, double sign, boolean delta) {
		return sign(value, sign(sign), delta);
	}

	public static double sign(double value, Sign sign, boolean delta) {
		if (sign == Sign.Zero) return 0;
		if (!delta) value = abs(value);
		switch (sign) {
			case Positive:
				return value;
			case Negative:
				return -value;
			default:
				throw new EnumException(Sign.class, sign);
		}
	}

	/**
	 * Return <code>-1</code>, <code>0</code> or <code>1</code> if the input is negative, zero or positive respectively.
	 * 
	 * @param value
	 *            The number to compute the signum of.
	 * @return <code>-1</code>, <code>0</code> or <code>1</code> if the input is negative, zero or positive respectively.
	 */
	public static Sign sign(float value) {
		if (Float.isNaN(value)) return null;
		return sign((int) Math.signum(value));
	}

	public static float sign(float value, float sign, boolean delta) {
		return sign(value, sign(sign), delta);
	}

	public static float sign(float value, Sign sign, boolean delta) {
		if (sign == Sign.Zero) return 0;
		if (!delta) value = abs(value);
		switch (sign) {
			case Positive:
				return value;
			case Negative:
				return -value;
			default:
				throw new EnumException(Sign.class, sign);
		}
	}

	/**
	 * Return <code>-1</code>, <code>0</code> or <code>1</code> if the input is negative, zero or positive respectively.
	 * 
	 * @param value
	 *            The number to compute the signum of.
	 * @return <code>-1</code>, <code>0</code> or <code>1</code> if the input is negative, zero or positive respectively.
	 */
	public static Sign sign(int value) {
		if (value < 0) return Sign.Negative;
		if (value > 0) return Sign.Positive;
		return Sign.Zero;
	}

	public static int sign(int value, int sign, boolean delta) {
		if (sign == 0) return 0;
		if (!delta) value = abs(value);
		return (sign > 0) ? value : -value;
	}

	public static int sign(int value, Sign sign, boolean delta) {
		if (sign == Sign.Zero) return 0;
		if (!delta) value = abs(value);
		switch (sign) {
			case Positive:
				return value;
			case Negative:
				return -value;
			default:
				throw new EnumException(Sign.class, sign);
		}
	}

	/**
	 * Return <code>-1</code>, <code>0</code> or <code>1</code> if the input is negative, zero or positive respectively.
	 * 
	 * @param value
	 *            The number to compute the signum of.
	 * @return <code>-1</code>, <code>0</code> or <code>1</code> if the input is negative, zero or positive respectively.
	 */
	public static Sign sign(long value) {
		if (value < 0) return Sign.Negative;
		if (value > 0) return Sign.Positive;
		return Sign.Zero;
	}

	public static long sign(long value, long sign, boolean delta) {
		if (sign == 0l) return 0l;
		if (!delta) value = abs(value);
		return (sign > 0l) ? value : -value;
	}

	public static long sign(long value, Sign sign, boolean delta) {
		if (sign == Sign.Zero) return 0;
		if (!delta) value = abs(value);
		switch (sign) {
			case Positive:
				return value;
			case Negative:
				return -value;
			default:
				throw new EnumException(Sign.class, sign);
		}
	}

	/**
	 * Return <code>-1</code>, <code>0</code> or <code>1</code> if the input is negative, zero or positive respectively.
	 * 
	 * @param value
	 *            The number to compute the signum of.
	 * @return <code>-1</code>, <code>0</code> or <code>1</code> if the input is negative, zero or positive respectively.
	 */
	public static Sign sign(short value) {
		if (value < 0) return Sign.Negative;
		if (value > 0) return Sign.Positive;
		return Sign.Zero;
	}

	public static short sign(short value, short sign, boolean delta) {
		if (sign == 0) return 0;
		if (!delta) value = abs(value);
		return (sign > 0) ? value : (short) -value;
	}

	public static short sign(short value, Sign sign, boolean delta) {
		if (sign == Sign.Zero) return 0;
		if (!delta) value = abs(value);
		switch (sign) {
			case Positive:
				return value;
			case Negative:
				return (short) -value;
			default:
				throw new EnumException(Sign.class, sign);
		}
	}

	public static double sqrt(double value) {
		return Math.sqrt(value);
	}
}
