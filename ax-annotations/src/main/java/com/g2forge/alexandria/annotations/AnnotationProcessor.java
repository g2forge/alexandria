package com.g2forge.alexandria.annotations;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
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

import lombok.RequiredArgsConstructor;

@SupportedAnnotationTypes({ "com.g2forge.alexandria.annotations.message.Hack", "com.g2forge.alexandria.annotations.message.TODO", "com.g2forge.alexandria.annotations.message.TODOs", "com.g2forge.alexandria.annotations.service.Service" })
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AnnotationProcessor extends AbstractProcessor {
	@SuppressWarnings("unchecked")
	protected static final Class<? extends Annotation>[] ANNOTATIONS = new Class[] { Hack.class, TODO.class, Service.class };

	protected static final Map<Class<? extends Annotation>, IAnnotationHandler<Annotation>> CACHE = new HashMap<>();

	@RequiredArgsConstructor
	protected static class PathSupplier implements Supplier<String> {
		protected final Element element;

		protected String value = null;

		@Override
		public String get() {
			if (value == null) {
				final LinkedList<Element> enclosing = new LinkedList<>();
				{ // Build a linked list of enclosing elements
					Element current = element;
					while (current != null) {
						enclosing.addFirst(current);
						current = current.getEnclosingElement();
					}
				}
				value = enclosing.stream().map(e -> e instanceof PackageElement ? ((PackageElement) e).getQualifiedName() : e.getSimpleName()).collect(Collectors.joining("."));
			}
			return value;
		}

		@Override
		public String toString() {
			return get();
		}
	}

	@Override
	public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {
		if (!roundEnvironment.processingOver()) {
			// Get the elements we might be interested in
			final Set<? extends Element> elements = typeElements.stream().flatMap(annotation -> roundEnvironment.getElementsAnnotatedWith(annotation).stream()).collect(Collectors.toSet());

			for (Element element : elements) {
				final Supplier<String> path = new PathSupplier(element);

				for (Class<? extends Annotation> annotationType : ANNOTATIONS) {
					// Get the actual annotations we'll need to handle (may be multiple due to repeating)
					final Annotation[] actualAnnotations = element.getAnnotationsByType(annotationType);
					if ((actualAnnotations == null) || (actualAnnotations.length < 1)) continue;

					// Get the handler, and cache it
					final IAnnotationHandler<Annotation> handler = CACHE.computeIfAbsent(annotationType, at -> {
						final Handler handlerAnnotation = at.getAnnotation(Handler.class);

						final Class<? extends IAnnotationHandler<?>> handlerType = handlerAnnotation.value();
						try {
							@SuppressWarnings({ "unchecked", "rawtypes" })
							final IAnnotationHandler<Annotation> temp = (IAnnotationHandler) handlerType.newInstance();
							return temp;
						} catch (InstantiationException | IllegalAccessException exception) {
							throw new Error(String.format("Failed to instantiate annotation handler for %1$s", at.getName()), exception);
						}
					});

					// Handle the annotations
					handler.handle(processingEnv, element, path, annotationType, actualAnnotations);
				}
			}
		}
		return true;
	}
}
