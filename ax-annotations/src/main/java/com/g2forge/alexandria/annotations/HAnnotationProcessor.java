package com.g2forge.alexandria.annotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public class HAnnotationProcessor {
	protected static AnnotationMirror getAnnotationMirror(Element element, Class<?> klass) {
		final String name = klass.getName();
		for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
			if (mirror.getAnnotationType().toString().equals(name)) { return mirror; }
		}
		throw new IllegalArgumentException("Could not find annotation of type \"" + name + "\"!");
	}

	protected static AnnotationValue getAnnotationValue(AnnotationMirror mirror, String name) {
		for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : mirror.getElementValues().entrySet()) {
			if (entry.getKey().getSimpleName().toString().equals(name)) { return entry.getValue(); }
		}
		throw new IllegalArgumentException("Could not find annotation value with name \"" + name + "\"!");
	}

	public static String getOption(ProcessingEnvironment processingEnvironment, Class<?> type, String key, String defaultValue) {
		return processingEnvironment.getOptions().getOrDefault(type.getName() + '.' + key, defaultValue);
	}

	public static List<? extends TypeMirror> getTypeMirrorAnnotationValue(Element element, Class<? extends Annotation> annotation, String name) {
		final AnnotationMirror mirror = getAnnotationMirror(element, annotation);
		final AnnotationValue value = getAnnotationValue(mirror, name);
		if (value.getValue() instanceof Collection) {
			return ((Collection<?>) value.getValue()).stream().map(AnnotationValue.class::cast).map(AnnotationValue::getValue).map(TypeMirror.class::cast).collect(Collectors.toList());
		} else {
			return Arrays.asList((TypeMirror) value.getValue());
		}
	}

	public static String toString(Throwable throwable) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (final PrintStream print = new PrintStream(baos)) {
			throwable.printStackTrace(print);
		}
		final String string = baos.toString();
		return string;
	}

	public static TypeElement toTypeElement(ProcessingEnvironment processingEnvironment, TypeMirror mirror) {
		return (TypeElement) processingEnvironment.getTypeUtils().asElement(mirror);
	}
}
