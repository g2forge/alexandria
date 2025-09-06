package com.g2forge.alexandria.java.io;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class TextRangeSpecifier {
	/** The starting line of the range. This is inclusive (this line is in range) and 0-based (the first line of the file is line 0). */
	protected final int startLine;

	/**
	 * The ending line of the range. This is exclusive (this line is not in range) and 0-based (the first line of the file is line 0). This may be the same as
	 * {@link #startLine} if the range is within that line.
	 */
	protected final int endLine;

	/**
	 * The starting character of the range, on the {@link #startLine}. This is inclusive (this character is in range) and 0-based (the first character of each
	 * line is 0).
	 */
	protected final int startCharacter;

	/**
	 * The ending character of the range, on the line before the {@link #endLine}. This is exclusive (this character is not in range) and 0-based (the first
	 * character of each line is 0).
	 */
	protected final int endCharacter;

	public TextRangeSpecifier(int line) {
		this(line, line + 1, 0, 0);
	}

	public TextRangeSpecifier(int startLine, int endLine) {
		this(startLine, endLine, 0, 0);

	}

	public TextRangeSpecifier(int startLine, int endLine, int startCharacter, int endCharacter) {
		this.startLine = startLine;
		this.endLine = endLine;
		this.startCharacter = startCharacter;
		this.endCharacter = endCharacter;

		if ((startLine < 0) || (endLine < 0) || (startCharacter < 0) || (endCharacter < 0)) throw new IllegalArgumentException("All inputs must be >=0");
		if (endLine < startLine) throw new IllegalArgumentException(String.format("Start line (%1$s) must be greater than or equal to end line (%2$s)", startLine, endLine));
		if ((getStartLine() == getEndLine()) && (endCharacter <= startCharacter)) throw new IllegalArgumentException(String.format("Start character (%1$s) must be strictly less than end character (%2$s), because this is a one line range (line %3$s)", startCharacter, endCharacter, startLine));
	}

	public boolean isSingleLine() {
		return getStartLine() == (getEndLine() - 1);
	}

	public String toRangeString() {
		final StringBuilder retVal = new StringBuilder();
		retVal.append(getStartLine());
		final int startCharacter = getStartCharacter(), endCharacter = getEndCharacter();
		final boolean printCharacters = ((startCharacter != 0) || (endCharacter != 0));
		if (printCharacters) retVal.append(':').append(startCharacter);
		if (printCharacters || !isSingleLine()) retVal.append(" to ").append(getEndLine());
		if (printCharacters) retVal.append(':').append(endCharacter);
		return retVal.toString();
	}

	@Override
	public String toString() {
		return toRangeString();
	}
}
