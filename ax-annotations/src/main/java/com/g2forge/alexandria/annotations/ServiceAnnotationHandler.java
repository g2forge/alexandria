package com.g2forge.alexandria.annotations;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

public class ServiceAnnotationHandler implements IAnnotationHandler<Service> {
	@Override
	public void handle(ProcessingEnvironment processingEnvironment, Element element, String path, Class<? extends Service> annotationType, Service annotation) {
		processingEnvironment.getFiler();
	}
}
