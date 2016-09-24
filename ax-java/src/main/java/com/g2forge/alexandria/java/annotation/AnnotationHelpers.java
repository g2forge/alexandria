package com.g2forge.alexandria.java.annotation;

import java.lang.annotation.Annotation;

import com.g2forge.alexandria.java.marker.Helpers;

import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class AnnotationHelpers {
	/**
	 * This is necessary because annotations on annotations don't translate through the dynamic proxy the JVM generates.
	 * 
	 * @param a
	 * @return
	 */
	public static Class<?> getAnnotationsOnAnnotation(Annotation a) {
		return a.getClass().getInterfaces()[0];
	}
}
