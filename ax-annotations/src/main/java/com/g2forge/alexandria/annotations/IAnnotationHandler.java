package com.g2forge.alexandria.annotations;

import java.lang.annotation.Annotation;
import java.util.function.Supplier;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

public interface IAnnotationHandler<T extends Annotation> {
	public void handle(ProcessingEnvironment processingEnvironment, Element element, Supplier<String> path, Class<? extends T> annotationType, @SuppressWarnings("unchecked") T... annotations);
}
