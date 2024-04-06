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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Supplier;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileManager.Location;
import javax.tools.StandardLocation;

import com.g2forge.alexandria.annotations.ElementAnnotations;
import com.g2forge.alexandria.annotations.HAnnotationProcessor;
import com.g2forge.alexandria.annotations.IAnnotationHandler;

public class ServiceAnnotationHandler implements IAnnotationHandler<Service> {
	protected static boolean ENABLE_DEBUGGING = false;
	
	protected static void debug(ProcessingEnvironment processingEnvironment, Supplier<String> message) {
		if (ENABLE_DEBUGGING) processingEnvironment.getMessager().printMessage(Diagnostic.Kind.WARNING, message.get());
	}
	
	protected static final Location[] READ_LOCATIONS = new Location[] { StandardLocation.CLASS_OUTPUT, StandardLocation.SOURCE_OUTPUT, StandardLocation.CLASS_PATH, StandardLocation.SOURCE_PATH };

	protected static Set<String> readImplementations(ProcessingEnvironment processingEnvironment, final String fileName, final Location location) throws IOException {
		final FileObject inputResource;

		try {
			inputResource = processingEnvironment.getFiler().getResource(location, "", fileName);
		} catch (FileNotFoundException | IllegalArgumentException exception) {
			debug(processingEnvironment, () -> HAnnotationProcessor.toString(exception));
			return Collections.emptySet();
		}

		try (final BufferedReader inputReader = new BufferedReader(inputResource.openReader(true))) {
			final Set<String> retVal = new LinkedHashSet<>();
			for (String line = inputReader.readLine(); line != null; line = inputReader.readLine()) {
				retVal.add(line);
			}
			return retVal;
		} catch (FileNotFoundException | NoSuchFileException exception) {
			debug(processingEnvironment, () -> HAnnotationProcessor.toString(exception));
			return Collections.emptySet();
		} catch (IOException exception) {
			debug(processingEnvironment, () -> HAnnotationProcessor.toString(exception));
			if (exception.getMessage().contains("does not exist")) return Collections.emptySet();
			throw exception;
		}
	}

	protected static void writeImplementations(final ProcessingEnvironment processingEnvironment, final StandardLocation location, final String fileName, Map<String, Element> implementations) throws IOException {
		debug(processingEnvironment, () -> "Writing " + fileName + " to " + location);
		final FileObject outputResource = processingEnvironment.getFiler().createResource(location, "", fileName, implementations.values().toArray(Element[]::new));
		try (final PrintStream outputStream = new PrintStream(outputResource.openOutputStream())) {
			implementations.keySet().forEach(outputStream::println);
		}
	}

	protected final Map<String, List<Element>> services = new HashMap<>();

	public void close(ProcessingEnvironment processingEnvironment) {
		for (Map.Entry<String, List<Element>> entry : services.entrySet()) {
			final String fileName = "META-INF/services/" + entry.getKey();
			
			try {
				final Set<String> existing;
				final Map<String, Element> implementations = new TreeMap<>();
				{
					final Elements elements = processingEnvironment.getElementUtils();
					Set<String> existing_ = null;
					for (Location location : READ_LOCATIONS) {
						final Set<String> loaded = readImplementations(processingEnvironment, fileName, location);
						if (location == StandardLocation.SOURCE_OUTPUT) existing_ = new TreeSet<>(loaded);
						for (String implementation : loaded) {
							final TypeElement element = elements.getTypeElement(implementation);
							if (element != null) implementations.put(implementation, element);
							else processingEnvironment.getMessager().printMessage(Diagnostic.Kind.WARNING, "Service implementation " + implementation + " does not exist, removing");
						}
					}
					existing = existing_;
				}
				
				for (Element element : entry.getValue()) {
					final TypeElement typeElement = (TypeElement) element;
					implementations.put(typeElement.getQualifiedName().toString(), element);
				}
				
				debug(processingEnvironment, () -> "Existing: " + existing.toString());
				debug(processingEnvironment, () -> "Implementations: " + implementations.keySet());
				if (existing == null || !existing.equals(implementations.keySet())) {
					writeImplementations(processingEnvironment, StandardLocation.CLASS_OUTPUT, fileName, implementations);
				}
			} catch (Throwable throwable) {
				final Messager messager = processingEnvironment.getMessager();
				for (Element element : entry.getValue()) {
					final String className = ((TypeElement) element).getQualifiedName().toString();
					final String message = "Could not create resource \"" + fileName + " to add service \"" + className + "\"\": " + HAnnotationProcessor.toString(throwable);
					messager.printMessage(Diagnostic.Kind.ERROR, message, element);
					debug(processingEnvironment, () -> message);
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
