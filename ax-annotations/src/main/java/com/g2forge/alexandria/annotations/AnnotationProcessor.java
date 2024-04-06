package com.g2forge.alexandria.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

import com.g2forge.alexandria.annotations.note.Note;
import com.g2forge.alexandria.annotations.note.RuntimeNote;
import com.g2forge.alexandria.annotations.service.Service;

import lombok.RequiredArgsConstructor;

@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class AnnotationProcessor extends AbstractProcessor {
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

	@SuppressWarnings("unchecked")
	protected static final Class<? extends Annotation>[] ANNOTATIONS = new Class[] { Service.class, Note.class, RuntimeNote.class };

	protected final Map<Class<? extends Annotation>, IAnnotationHandler<Annotation>> handlers = new HashMap<>();

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Stream.of(ANNOTATIONS).flatMap(type -> Stream.of(type, type.isAnnotationPresent(Repeatable.class) ? type.getAnnotation(Repeatable.class).value() : null)).filter(Objects::nonNull).map(type -> type.getName()).collect(Collectors.toSet());
	}

	@Override
	public Set<String> getSupportedOptions() {
		return Stream.of(ANNOTATIONS).flatMap(type -> Stream.of(type.getAnnotation(Handler.class).options()).map(option -> type.getName() + '.' + option)).collect(Collectors.toSet());
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
		try {
			// Get the elements we might be interested in
			final Set<? extends Element> elements = annotations.stream().flatMap(annotation -> roundEnvironment.getElementsAnnotatedWith(annotation).stream()).collect(Collectors.toSet());

			final Map<IAnnotationHandler<Annotation>, Collection<ElementAnnotations<Annotation>>> elementAnnotations = new IdentityHashMap<>();
			for (Element element : elements) {
				final Supplier<String> path = new PathSupplier(element);

				for (Class<? extends Annotation> annotationType : ANNOTATIONS) {
					// Get the actual annotations we'll need to handle (may be multiple due to repeating)
					final Annotation[] actualAnnotations = element.getAnnotationsByType(annotationType);
					if ((actualAnnotations == null) || (actualAnnotations.length < 1)) continue;

					// Get the handler, and cache it
					final IAnnotationHandler<Annotation> handler = handlers.computeIfAbsent(annotationType, at -> {
						final Handler handlerAnnotation = at.getAnnotation(Handler.class);
						if (handlerAnnotation == null) throw new Error(String.format("Annotation type \"%1$s\" was not annotated with a handler!", at.getName()));

						final Class<? extends IAnnotationHandler<?>> handlerType = handlerAnnotation.value();
						try {
							Constructor<? extends IAnnotationHandler<?>> constructor = handlerType.getConstructor();
							@SuppressWarnings({ "unchecked", "rawtypes" })
							final IAnnotationHandler<Annotation> temp = (IAnnotationHandler) constructor.newInstance();
							return temp;
						} catch (Throwable throwable) {
							throw new Error(String.format("Failed to instantiate annotation handler for %1$s", at.getName()), throwable);
						}
					});

					// Save the annotations so that we only call the handler once
					elementAnnotations.computeIfAbsent(handler, h -> new ArrayList<>()).add(new ElementAnnotations<>(element, path, annotationType, actualAnnotations));
				}
			}

			for (Map.Entry<IAnnotationHandler<Annotation>, Collection<ElementAnnotations<Annotation>>> entry : elementAnnotations.entrySet()) {
				// Handle the annotations
				entry.getKey().handle(processingEnv, entry.getValue());
			}
		} finally {
			if (roundEnvironment.processingOver()) handlers.values().forEach(handler -> handler.close(processingEnv));
		}
		return true;
	}
}
