package com.g2forge.alexandria.annotations.note;

import javax.tools.Diagnostic;

public enum NoteType {
	Note(null, Diagnostic.Kind.WARNING),
	TODO(null, Diagnostic.Kind.WARNING),
	Hack(null, Diagnostic.Kind.WARNING),
	RFC("Request for comment", Diagnostic.Kind.WARNING),
	RFP("Request for proposal", Diagnostic.Kind.WARNING),
	RFD("Request for documentation", Diagnostic.Kind.WARNING),
	Error(null, Diagnostic.Kind.ERROR);

	protected final String message;

	protected final Diagnostic.Kind kind;

	private NoteType(String message, Diagnostic.Kind kind) {
		if (kind == null) throw new IllegalArgumentException();
		this.message = message == null ? name() : message;
		this.kind = kind;
	}
}
