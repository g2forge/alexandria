package com.g2forge.alexandria.annotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

public class StandardMessageAnnotationHandler implements IAnnotationHandler<Annotation> {
	@Override
	public void handle(ProcessingEnvironment processingEnvironment, Element element, String path, Class<? extends Annotation> annotationType, Annotation annotation) {
		final Message message = annotationType.getAnnotation(Message.class);

		String value = null;
		try {
			value = (String) annotation.getClass().getMethod("value").invoke(annotation);
		} catch (Throwable throwable) {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (final PrintStream print = new PrintStream(baos)) {
				throwable.printStackTrace(print);
			}
			processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, "Could not get message value: " + baos.toString(), element);
		}

		final String string = String.format(message.value(), path);
		processingEnvironment.getMessager().printMessage(message.kind(), ((value != null) && (value.length() > 0)) ? (string + ": " + value) : string, element);
	}
}
