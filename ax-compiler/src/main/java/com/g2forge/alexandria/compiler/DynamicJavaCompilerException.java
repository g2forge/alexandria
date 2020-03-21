package com.g2forge.alexandria.compiler;

import java.util.Collection;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import com.g2forge.alexandria.java.text.HString;

import lombok.Getter;

public class DynamicJavaCompilerException extends RuntimeException {
	private static final long serialVersionUID = 9088488842701040368L;

	public static String getMessage(Collection<? extends Diagnostic<? extends JavaFileObject>> diagnostics) {
		final StringBuilder retVal = new StringBuilder();
		for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
			final JavaFileObject source = diagnostic.getSource();
			if (source != null) retVal.append(String.format("%s:%d ", source.toUri(), diagnostic.getLineNumber()));

			final String message = diagnostic.getMessage(Locale.ENGLISH);
			final String kind = HString.lowercase(diagnostic.getKind().toString());
			retVal.append(String.format("%s: %s%n", kind, message));
		}
		return retVal.toString();
	}

	@Getter
	protected final Collection<? extends Diagnostic<? extends JavaFileObject>> diagnostics;

	public DynamicJavaCompilerException(Collection<? extends Diagnostic<? extends JavaFileObject>> diagnostics) {
		super(getMessage(diagnostics));
		this.diagnostics = diagnostics;
	}
}
