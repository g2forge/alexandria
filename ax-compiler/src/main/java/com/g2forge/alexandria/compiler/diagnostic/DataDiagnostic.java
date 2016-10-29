package com.g2forge.alexandria.compiler.diagnostic;

import java.util.Locale;

import javax.tools.Diagnostic;

import lombok.Data;

@Data
public class DataDiagnostic<S> implements Diagnostic<S> {
	protected final Diagnostic.Kind kind;

	protected final S source;

	protected final long line;

	protected final long position;

	protected final String code;

	protected final String message;

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public long getColumnNumber() {
		return Diagnostic.NOPOS;
	}

	@Override
	public long getEndPosition() {
		return getPosition();
	}

	@Override
	public long getLineNumber() {
		return line;
	}

	@Override
	public String getMessage(Locale locale) {
		return message;
	}

	@Override
	public long getStartPosition() {
		return getPosition();
	}
}
