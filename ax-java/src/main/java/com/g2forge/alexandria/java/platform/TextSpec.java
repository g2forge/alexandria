package com.g2forge.alexandria.java.platform;

import com.g2forge.alexandria.java.text.eol.EOLStyle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TextSpec {
	DOS(EOLStyle.DOS, EOFStyle.Add, false),
	UNIX(EOLStyle.UNIX, EOFStyle.Remove, true);

	public static enum EOFStyle {
		Add,
		Remove,
		Ignore;
	}

	protected final EOLStyle eol;

	protected final EOFStyle eof;

	protected final boolean finalEOL;
}
