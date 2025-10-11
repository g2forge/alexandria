package com.g2forge.alexandria.java.adt.compare;

import java.util.Comparator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AlphaNumericChunkComparator implements Comparator<String> {
	protected final Comparator<String> nonNumericComparator;

	public AlphaNumericChunkComparator() {
		this(Comparator.naturalOrder());
	}

	private final String getChunk(String input, int inputLength, int index) {
		final StringBuilder retVal = new StringBuilder();
		char current = input.charAt(index);
		retVal.append(current);
		index++;
		final boolean isNumeric = Character.isDigit(current);
		while (index < inputLength) {
			current = input.charAt(index);
			if (Character.isDigit(current) != isNumeric) break;
			retVal.append(current);
			index++;
		}
		return retVal.toString();
	}

	public int compare(String s1, String s2) {
		int s1Index = 0, s2Index = 0;
		final int s1Length = s1.length(), s2Length = s2.length();

		while ((s1Index < s1Length) && (s2Index < s2Length)) {
			final String s1Chunk = getChunk(s1, s1Length, s1Index), s2Chunk = getChunk(s2, s2Length, s2Index);
			final int s1ChunkLength = s1Chunk.length(), s2ChunkLength = s2Chunk.length();
			s1Index += s1ChunkLength;
			s2Index += s2ChunkLength;

			if (Character.isDigit(s1Chunk.charAt(0)) && Character.isDigit(s2Chunk.charAt(0))) {
				if (s1ChunkLength != s2ChunkLength) return s1ChunkLength - s2ChunkLength;
				for (int i = 0; i < s1ChunkLength; i++) {
					final int retVal = s1Chunk.charAt(i) - s2Chunk.charAt(i);
					if (retVal != 0) { return retVal; }
				}
			} else {
				final int retVal = getNonNumericComparator().compare(s1Chunk, s2Chunk);
				if (retVal != 0) return retVal;
			}
		}

		return s1Length - s2Length;
	}

}
