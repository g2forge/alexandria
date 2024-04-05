package com.g2forge.alexandria.annotations.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileManager.Location;
import javax.tools.StandardLocation;

import com.g2forge.alexandria.annotations.ElementAnnotations;
import com.g2forge.alexandria.annotations.HAnnotationProcessor;
import com.g2forge.alexandria.annotations.IAnnotationHandler;

public class ServiceAnnotationHandler implements IAnnotationHandler<Service> {
	protected static final Location[] READ_LOCATIONS = new Location[] { StandardLocation.CLASS_OUTPUT, StandardLocation.SOURCE_OUTPUT, StandardLocation.CLASS_PATH, StandardLocation.SOURCE_PATH };

	protected static void readImplementations(final Collection<String> retVal, ProcessingEnvironment processingEnvironment, final String fileName, final Location location) throws IOException {
		final FileObject inputResource;

		try {
			inputResource = processingEnvironment.getFiler().getResource(location, "", fileName);
		} catch (FileNotFoundException | IllegalArgumentException exception) {
			// processingEnvironment.getMessager().printMessage(Diagnostic.Kind.WARNING, HAnnotationProcessor.toString(exception));
			return;
		}

		try (final BufferedReader inputReader = new BufferedReader(inputResource.openReader(true))) {
			for (String line = inputReader.readLine(); line != null; line = inputReader.readLine()) {
				retVal.add(line);
			}
		} catch (FileNotFoundException | NoSuchFileException exception) {
			// processingEnvironment.getMessager().printMessage(Diagnostic.Kind.WARNING, HAnnotationProcessor.toString(exception));
			return;
		} catch (IOException exception) {
			// processingEnvironment.getMessager().printMessage(Diagnostic.Kind.WARNING, HAnnotationProcessor.toString(exception));
			if (exception.getMessage().contains("does not exist")) return;
			throw exception;
		}
	}

	protected static void writeImplementations(final ProcessingEnvironment processingEnvironment, final StandardLocation location, final String fileName, List<Element> elements, final List<String> list) throws IOException {
		final FileObject outputResource = processingEnvironment.getFiler().createResource(location, "", fileName, elements.toArray(new Element[0]));
		try (final PrintStream outputStream = new PrintStream(outputResource.openOutputStream())) {
			list.forEach(outputStream::println);
		}
	}

	protected final Map<String, List<Element>> services = new HashMap<>();

	public void close(ProcessingEnvironment processingEnvironment) {
		for (Map.Entry<String, List<Element>> entry : services.entrySet()) {
			final String fileName = "META-INF/services/" + entry.getKey();
			final Set<String> implementations = new HashSet<>();

			try {
				for (Location location : READ_LOCATIONS) {
					readImplementations(implementations, processingEnvironment, fileName, location);
				}

				final Set<String> newServices = entry.getValue().stream().map(TypeElement.class::cast).map(TypeElement::getQualifiedName).map(Object::toString).collect(Collectors.toSet());
				if (implementations.addAll(newServices)) {
					final List<String> sorted = new ArrayList<>(implementations);
					Collections.sort(sorted);
					writeImplementations(processingEnvironment, StandardLocation.SOURCE_OUTPUT, fileName, entry.getValue(), sorted);
				}
			} catch (Throwable throwable) {
				final Messager messager = processingEnvironment.getMessager();
				for (Element element : entry.getValue()) {
					final String className = ((TypeElement) element).getQualifiedName().toString();
					messager.printMessage(Diagnostic.Kind.ERROR, "Could not create resource \"" + fileName + " to add service \"" + className + "\"\": " + HAnnotationProcessor.toString(throwable), element);
				}
			}
		}
	}

	@Override
	public void handle(ProcessingEnvironment processingEnvironment, Collection<ElementAnnotations<Service>> elementAnnotations) {
		for (ElementAnnotations<Service> elementAnnotation : elementAnnotations) {
			final List<? extends TypeMirror> typeMirrors = HAnnotationProcessor.getTypeMirrorAnnotationValue(elementAnnotation.getElement(), Service.class, "value");
			for (TypeMirror service : typeMirrors) {
				final TypeElement serviceElement = HAnnotationProcessor.toTypeElement(processingEnvironment, service);
				final Name serviceName = processingEnvironment.getElementUtils().getBinaryName(serviceElement);
				services.computeIfAbsent(serviceName.toString(), sn -> new ArrayList<>()).add(elementAnnotation.getElement());
			}
		}
	}
}
