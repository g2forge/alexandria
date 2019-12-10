package com.g2forge.alexandria.java.text;

public class CharSubSequence implements CharSequence {
	protected final CharSequence sequence;

	protected final int offset;

	protected final int length;

	public CharSubSequence(CharSequence sequence, int offset) {
		this(sequence, offset, sequence.length() - offset);
	}

	public CharSubSequence(CharSequence sequence, int offset, int length) {
		this.sequence = sequence;
		this.offset = offset;
		this.length = length;

		if ((offset < 0) || (offset > sequence.length())) throw new IndexOutOfBoundsException(String.format("Bad offset %1$d, should be in range [0,%2$d])!", offset, sequence.length()));
		if ((length < 0) || (length > sequence.length())) throw new IndexOutOfBoundsException(String.format("Bad length %1$d, should be in range [0,%2$d])!", length, sequence.length()));
		if (offset + length > sequence.length()) throw new IndexOutOfBoundsException(String.format("Bad ending offiset %1$d, should be in range [0,%2$d])!", offset + length, sequence.length()));
	}

	@Override
	public char charAt(int index) {
		if (index < 0 || index > length) throw new IndexOutOfBoundsException();
		return sequence.charAt(offset + index);
	}

	@Override
	public int length() {
		return length;
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return new CharSubSequence(sequence, offset + start, end - start);
	}

	@Override
	public String toString() {
		final char[] chars = new char[length()];
		for (int i = 0; i < chars.length; i++)
			chars[i] = charAt(i);
		return new String(chars);
	}
}