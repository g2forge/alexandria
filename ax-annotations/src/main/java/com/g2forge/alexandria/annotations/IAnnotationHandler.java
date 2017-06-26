package com.g2forge.alexandria.annotations;

import java.lang.annotation.Annotation;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

public interface IAnnotationHandler<T extends Annotation> {
	public void handle(ProcessingEnvironment processingEnvironment, Element element, String path, Class<? extends T> annotationType, T annotation);
}
