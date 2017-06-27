package com.g2forge.alexandria.annotations.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import com.g2forge.alexandria.annotations.HAnnotationProcessor;
import com.g2forge.alexandria.annotations.IAnnotationHandler;

public class ServiceAnnotationHandler implements IAnnotationHandler<Service> {
	protected void addImplementation(ProcessingEnvironment processingEnvironment, Element element, final Name className, final Object serviceName) {
		final Filer filer = processingEnvironment.getFiler();
		final Messager messager = processingEnvironment.getMessager();

		final String fileName = "META-INF/services/" + serviceName;
		final Set<String> implementations = new HashSet<>();

		try {
			readImplementations(implementations, filer, fileName, StandardLocation.SOURCE_PATH);
			readImplementations(implementations, filer, fileName, StandardLocation.SOURCE_OUTPUT);

			if (implementations.add(className.toString())) {
				final List<String> list = new ArrayList<>(implementations);
				Collections.sort(list);

				writeImplementations(filer, StandardLocation.SOURCE_OUTPUT, fileName, element, list);
				writeImplementations(filer, StandardLocation.CLASS_OUTPUT, fileName, element, list);
			}
		} catch (Throwable throwable) {
			messager.printMessage(Diagnostic.Kind.ERROR, "Could not create resource \"" + fileName + " to add service \"" + className + "\"\": " + HAnnotationProcessor.toString(throwable), element);
		}
	}

	@Override
	public void handle(ProcessingEnvironment processingEnvironment, Element element, String path, Class<? extends Service> annotationType, Service annotation) {
		final Name className = ((TypeElement) element).getQualifiedName();

		final List<? extends TypeMirror> typeMirrors = HAnnotationProcessor.getTypeMirrorAnnotationValue(element, Service.class, "value");
		for (TypeMirror service : typeMirrors) {
			final TypeElement serviceElement = HAnnotationProcessor.toTypeElement(processingEnvironment, service);
			final Name serviceName = processingEnvironment.getElementUtils().getBinaryName(serviceElement);
			addImplementation(processingEnvironment, element, className, serviceName);
		}
	}

	protected void readImplementations(final Collection<String> retVal, final Filer filer, final String fileName, final StandardLocation location) throws IOException {
		final FileObject inputResource;
		try {
			inputResource = filer.getResource(location, "", fileName);
		} catch (FileNotFoundException | IllegalArgumentException exception) {
			return;
		}

		try {
			try (final BufferedReader inputReader = new BufferedReader(inputResource.openReader(true))) {
				for (String line = inputReader.readLine(); line != null; line = inputReader.readLine()) {
					retVal.add(line);
				}
			}
		} catch (IOException exception) {
			return;
		}
	}

	protected void writeImplementations(final Filer filer, final StandardLocation location, final String fileName, Element element, final List<String> list) throws IOException {
		final FileObject outputResource = filer.createResource(location, "", fileName, element);
		try (final PrintStream outputStream = new PrintStream(outputResource.openOutputStream())) {
			list.forEach(outputStream::println);
		}
	}
}
