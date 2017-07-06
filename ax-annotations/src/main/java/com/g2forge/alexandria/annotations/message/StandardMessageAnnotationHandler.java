package com.g2forge.alexandria.annotations.message;

import java.lang.annotation.Annotation;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

import com.g2forge.alexandria.annotations.HAnnotationProcessor;
import com.g2forge.alexandria.annotations.IAnnotationHandler;

public class StandardMessageAnnotationHandler implements IAnnotationHandler<Annotation> {
	@Override
	public void handle(ProcessingEnvironment processingEnvironment, Element element, String path, Class<? extends Annotation> annotationType, Annotation annotation) {
		final Message message = annotationType.getAnnotation(Message.class);

		String value = null;
		try {
			value = (String) annotation.getClass().getMethod("value").invoke(annotation);
		} catch (Throwable throwable) {
			processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, "Could not get message value: " + HAnnotationProcessor.toString(throwable), element);
		}

		final String string = String.format(message.value(), path);
		processingEnvironment.getMessager().printMessage(message.kind(), ((value != null) && (value.length() > 0)) ? (string + ": " + value) : string, element);
	}
}
