package com.g2forge.alexandria.compiler.diagnostic;

import javax.tools.Diagnostic;

import com.g2forge.alexandria.test.FieldMatcher;

public class DiagnosticMatcher<S> extends FieldMatcher<Diagnostic<S>> {
	protected static final FieldMatcher.FieldSet<Diagnostic<?>> FIELDS = new FieldMatcher.FieldSet<Diagnostic<?>>(Diagnostic::getKind, Diagnostic::getLineNumber, Diagnostic::getPosition, Diagnostic::getCode);

	public DiagnosticMatcher(Diagnostic<S> expected) {
		super(expected, FIELDS);
	}
}
