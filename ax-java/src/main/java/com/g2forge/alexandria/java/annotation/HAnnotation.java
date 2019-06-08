package com.g2forge.alexandria.java.annotation;

import java.lang.annotation.Annotation;

import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HAnnotation {
	/**
	 * Get the annotations on an annotation itself. This is necessary because annotations on annotations don't translate through the dynamic proxy the JVM
	 * generates.
	 * 
	 * @param annotation The annotation to get the annotations on.
	 * @return Any annotations on the <code>annotation</code>.
	 */
	public static Class<?> getAnnotationsOnAnnotation(Annotation annotation) {
		return annotation.getClass().getInterfaces()[0];
	}
}
