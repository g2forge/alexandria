package com.g2forge.alexandria.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.PACKAGE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE })
@MessageAnnotation(value = "TODO", handler = TODOMessageAnnotationHandler.class)
public @interface TODO {
	public String value() default "";

	public String user() default "";

	public String link() default "";

	public String deadline() default "";
}
