package com.g2forge.alexandria.metadata.v1;

import java.lang.annotation.Annotation;

import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HAnnotation {
	/**
	 * This is necessary because annotations on annotations don't translate through the dynamic proxy the JVM generates.
	 * 
	 * @param annotation
	 * @return
	 */
	public static Class<?> getAnnotationsOnAnnotation(Annotation annotation) {
		return annotation.getClass().getInterfaces()[0];
	}
}
