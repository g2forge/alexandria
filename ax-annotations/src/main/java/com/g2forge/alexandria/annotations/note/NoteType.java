package com.g2forge.alexandria.annotations.note;

import javax.tools.Diagnostic;

public enum NoteType {
	Note(null, Diagnostic.Kind.NOTE),
	TODO(null, Diagnostic.Kind.NOTE),
	Hack(null, Diagnostic.Kind.WARNING),
	RFC("Request for comment", Diagnostic.Kind.NOTE),
	RFP("Request for proposal", Diagnostic.Kind.NOTE),
	RFD("Request for documentation", Diagnostic.Kind.NOTE),
	Error(null, Diagnostic.Kind.ERROR);

	protected final String message;

	protected final Diagnostic.Kind kind;

	private NoteType(String message, Diagnostic.Kind kind) {
		if (kind == null) throw new IllegalArgumentException();
		this.message = message == null ? name() : message;
		this.kind = kind;
	}
}
