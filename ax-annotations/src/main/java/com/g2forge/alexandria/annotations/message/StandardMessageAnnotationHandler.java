package com.g2forge.alexandria.annotations.message;

import java.lang.annotation.Annotation;
import java.util.Collection;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;

import com.g2forge.alexandria.annotations.ElementAnnotations;
import com.g2forge.alexandria.annotations.HAnnotationProcessor;
import com.g2forge.alexandria.annotations.IAnnotationHandler;

public class StandardMessageAnnotationHandler implements IAnnotationHandler<Annotation> {
	@Override
	public void handle(ProcessingEnvironment processingEnvironment, Collection<ElementAnnotations<Annotation>> elementAnnotations) {
		final Messager messager = processingEnvironment.getMessager();

		for (ElementAnnotations<Annotation> elementAnnotation : elementAnnotations) {
			final Message message = elementAnnotation.getType().getAnnotation(Message.class);
			for (Annotation annotation : elementAnnotation.getAnnotations()) {
				String value = null;

				try {
					value = (String) annotation.getClass().getMethod("value").invoke(annotation);
				} catch (Throwable throwable) {
					messager.printMessage(Diagnostic.Kind.ERROR, "Could not get message value: " + HAnnotationProcessor.toString(throwable), elementAnnotation.getElement());
				}

				final String string = String.format(message.value(), elementAnnotation.getPath().get());
				messager.printMessage(message.kind(), ((value != null) && (value.length() > 0)) ? (string + ": " + value) : string, elementAnnotation.getElement());
			}
		}
	}
}
