package com.g2forge.alexandria.annotations.message;

import java.lang.annotation.Annotation;
import java.util.function.Supplier;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

import com.g2forge.alexandria.annotations.HAnnotationProcessor;
import com.g2forge.alexandria.annotations.IAnnotationHandler;

public class StandardMessageAnnotationHandler implements IAnnotationHandler<Annotation> {
	@Override
	public void handle(ProcessingEnvironment processingEnvironment, Element element, Supplier<String> path, Class<? extends Annotation> annotationType, Annotation... annotations) {
		final Message message = annotationType.getAnnotation(Message.class);
		final Messager messager = processingEnvironment.getMessager();

		for (Annotation annotation : annotations) {
			String value = null;

			try {
				value = (String) annotation.getClass().getMethod("value").invoke(annotation);
			} catch (Throwable throwable) {
				messager.printMessage(Diagnostic.Kind.ERROR, "Could not get message value: " + HAnnotationProcessor.toString(throwable), element);
			}

			final String string = String.format(message.value(), path);
			messager.printMessage(message.kind(), ((value != null) && (value.length() > 0)) ? (string + ": " + value) : string, element);
		}
	}
}
