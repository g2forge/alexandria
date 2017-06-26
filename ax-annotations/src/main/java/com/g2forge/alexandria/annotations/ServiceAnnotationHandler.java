package com.g2forge.alexandria.annotations;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

public class ServiceAnnotationHandler implements IAnnotationHandler<Service> {
	@Override
	public void handle(ProcessingEnvironment processingEnvironment, Element element, String path, Class<? extends Service> annotationType, Service annotation) {
		final Filer filer = processingEnvironment.getFiler();
		final Name className = ((TypeElement) element).getQualifiedName();

		List<? extends TypeMirror> services = null;
		try {
			annotation.value();
		} catch (MirroredTypesException exception) {
			services = exception.getTypeMirrors();
		}

		for (TypeMirror service : services) {
			final TypeElement serviceElement = (TypeElement) processingEnvironment.getTypeUtils().asElement(service);
			final Name serviceName = processingEnvironment.getElementUtils().getBinaryName(serviceElement);
			final String fileName = "META-INF/services/" + serviceName;
			final List<String> implementations = new ArrayList<>();
			implementations.add(className.toString());

			try {
				FileObject inputResource = null;
				try {
					inputResource = filer.getResource(StandardLocation.SOURCE_OUTPUT, "", fileName);
				} catch (IOException exception) {}

				if (inputResource != null) try {
					try (final BufferedReader inputReader = new BufferedReader(inputResource.openReader(true))) {
						for (String line = inputReader.readLine(); line != null; line = inputReader.readLine()) {
							implementations.add(line);
						}
					}
				} catch (FileNotFoundException exception) {}
				Collections.sort(implementations);

				final FileObject outputResource = filer.createResource(StandardLocation.SOURCE_OUTPUT, "", fileName, element);
				try (final PrintStream outputStream = new PrintStream(outputResource.openOutputStream())) {
					for (String implementation : implementations) {
						outputStream.println(implementation);
					}
				}
			} catch (Throwable throwable) {
				processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, "Could not create resource \"" + fileName + " to add service \"" + className + "\"\": " + throwable, element);
			}

			processingEnvironment.getMessager().printMessage(Diagnostic.Kind.NOTE, "Generated \"" + fileName + "\" with implementations " + implementations, element);
		}
	}
}
