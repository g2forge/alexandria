package com.g2forge.alexandria.parse.regex;

import java.util.regex.Pattern;

import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.parse.IPattern;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Regex implements IPattern, ISingleton {
	protected final Pattern pattern;

	protected final int nGroups;

	protected final Group<?, ?> group;

	@Override
	public String toString() {
		return getPattern().toString();
	}
}
