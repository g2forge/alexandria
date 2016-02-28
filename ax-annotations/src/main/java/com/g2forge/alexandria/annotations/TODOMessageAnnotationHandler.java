package com.g2forge.alexandria.annotations;

import java.time.LocalDate;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

public class TODOMessageAnnotationHandler implements IMessageAnnotationHandler<TODO> {
	public static boolean isNonEmpty(final String string) {
		return (string != null) && (string.length() > 0);
	}

	@Override
	public void handle(ProcessingEnvironment processingEnvironment, Element element, String path, MessageAnnotation messageAnnotation, TODO todo) {
		final boolean error;
		if (todo.deadline().length() > 0) {
			boolean _error = false;
			try {
				final LocalDate deadline = LocalDate.parse(todo.deadline());
				_error = LocalDate.now().compareTo(deadline) >= 0;
			} catch (Throwable throwable) {
				processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, "TODO deadline \"" + todo.deadline() + "\" could not be parsed: " + throwable, element);
			}
			error = _error;
		} else error = false;
		final StringBuilder builder = new StringBuilder();

		builder.append("TODO");
		final String user = todo.user(), link = todo.link();
		final boolean hasUser = isNonEmpty(user), hasLink = isNonEmpty(link);
		if (hasUser || hasLink) {
			builder.append("(");
			if (hasUser) builder.append(user);
			if (hasUser && hasLink) builder.append(", ");
			if (hasLink) builder.append(link);
			builder.append(")");
		}

		final String message = todo.value();
		if (isNonEmpty(message)) {
			builder.append(": ");
			builder.append(message);
		}
		if (error) builder.append(" (Deadline has passed, so this is now an error)");

		processingEnvironment.getMessager().printMessage(error ? Diagnostic.Kind.ERROR : messageAnnotation.kind(), builder.toString(), element);
	}
}
