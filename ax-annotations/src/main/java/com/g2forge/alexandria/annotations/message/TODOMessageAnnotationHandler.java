package com.g2forge.alexandria.annotations.message;

import java.time.LocalDate;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

import com.g2forge.alexandria.annotations.IAnnotationHandler;

public class TODOMessageAnnotationHandler implements IAnnotationHandler<TODO> {
	public static boolean isNonEmpty(final String string) {
		return (string != null) && (string.length() > 0);
	}

	@Override
	public void handle(ProcessingEnvironment processingEnvironment, Element element, String path, Class<? extends TODO> annotationType, TODO annotation) {
		final Message message = annotationType.getAnnotation(Message.class);

		final boolean error;
		if (annotation.deadline().length() > 0) {
			boolean _error = false;
			try {
				final LocalDate deadline = LocalDate.parse(annotation.deadline());
				_error = LocalDate.now().compareTo(deadline) >= 0;
			} catch (Throwable throwable) {
				processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, "TODO deadline \"" + annotation.deadline() + "\" could not be parsed: " + throwable, element);
			}
			error = _error;
		} else error = false;
		final StringBuilder builder = new StringBuilder();

		builder.append("TODO");
		final String user = annotation.user(), link = annotation.link();
		final boolean hasUser = isNonEmpty(user), hasLink = isNonEmpty(link);
		if (hasUser || hasLink) {
			builder.append("(");
			if (hasUser) builder.append(user);
			if (hasUser && hasLink) builder.append(", ");
			if (hasLink) builder.append(link);
			builder.append(")");
		}

		final String string = annotation.value();
		if (isNonEmpty(string)) {
			builder.append(": ");
			builder.append(string);
		}
		if (error) builder.append(" (Deadline has passed, so this is now an error)");

		processingEnvironment.getMessager().printMessage(error ? Diagnostic.Kind.ERROR : message.kind(), builder.toString(), element);
	}
}
