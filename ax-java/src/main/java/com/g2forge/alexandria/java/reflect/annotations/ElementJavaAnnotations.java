package com.g2forge.alexandria.java.reflect.annotations;

import java.lang.reflect.AnnotatedElement;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class ElementJavaAnnotations implements IElementJavaAnnotations {
	protected final AnnotatedElement annotatedElement;
}
