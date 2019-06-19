package com.g2forge.alexandria.annotations.message;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.g2forge.alexandria.annotations.Handler;

@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.PACKAGE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE })
@Handler(TODOMessageAnnotationHandler.class)
@Message("TODO")
@Repeatable(TODOs.class)
public @interface TODO {
	/**
	 * A deadline, past which this TODO annotation should become an error rather than a warning. This can be helpful to make sure things don't get ignored.
	 */
	public String deadline() default "";

	public String link() default "";

	public String user() default "";

	/**
	 * A message which should be reported during compilation.
	 */
	public String value() default "";
}
