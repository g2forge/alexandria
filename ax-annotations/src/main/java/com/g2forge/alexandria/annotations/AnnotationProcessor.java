package com.g2forge.alexandria.annotations;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

import com.g2forge.alexandria.annotations.message.Hack;
import com.g2forge.alexandria.annotations.message.TODO;
import com.g2forge.alexandria.annotations.service.Service;

@SupportedAnnotationTypes({ "com.g2forge.alexandria.annotations.message.Hack", "com.g2forge.alexandria.annotations.message.TODO", "com.g2forge.alexandria.annotations.message.TODOs", "com.g2forge.alexandria.annotations.service.Service" })
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AnnotationProcessor extends AbstractProcessor {
	@SuppressWarnings("unchecked")
	protected static final Class<? extends Annotation>[] ANNOTATIONS = new Class[] { Hack.class, TODO.class, Service.class };

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
		if (!roundEnvironment.processingOver()) {
			final Set<? extends Element> elements = annotations.stream().flatMap(annotation -> roundEnvironment.getElementsAnnotatedWith(annotation).stream()).collect(Collectors.toSet());
			for (Element element : elements) {
				final LinkedList<Element> enclosing = new LinkedList<>();
				Element current = element;
				while (current != null) {
					enclosing.addFirst(current);
					current = current.getEnclosingElement();
				}
				final String path = enclosing.stream().map(e -> e instanceof PackageElement ? ((PackageElement) e).getQualifiedName() : e.getSimpleName()).collect(Collectors.joining("."));

				for (Class<? extends Annotation> annotationType : ANNOTATIONS) {
					final Annotation[] actualAnnotations = element.getAnnotationsByType(annotationType);
					if ((actualAnnotations == null) || (actualAnnotations.length < 1)) continue;
					final Handler handlerAnnotation = annotationType.getAnnotation(Handler.class);

					final Class<? extends IAnnotationHandler<?>> handlerType = handlerAnnotation.value();
					final IAnnotationHandler<Annotation> handler;
					try {
						@SuppressWarnings({ "unchecked", "rawtypes" })
						final IAnnotationHandler<Annotation> temp = (IAnnotationHandler) handlerType.newInstance();
						handler = temp;
					} catch (InstantiationException | IllegalAccessException e) {
						throw new Error(e);
					}

					for (Annotation actualAnnotation : actualAnnotations) {
						handler.handle(processingEnv, element, path, annotationType, actualAnnotation);
					}
				}
			}
		}
		return true;
	}
}
