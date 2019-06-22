package com.g2forge.alexandria.annotations;

import java.lang.annotation.Annotation;
import java.util.Collection;

import javax.annotation.processing.ProcessingEnvironment;

public interface IAnnotationHandler<T extends Annotation> {
	public default void close(ProcessingEnvironment processingEnvironment) {}

	public void handle(ProcessingEnvironment processingEnvironment, Collection<ElementAnnotations<T>> elementAnnotations);
}
