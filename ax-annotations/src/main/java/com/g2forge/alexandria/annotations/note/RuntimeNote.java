package com.g2forge.alexandria.annotations.note;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.g2forge.alexandria.annotations.Handler;
import com.g2forge.alexandria.annotations.note.Note.NoRef;

/**
 * Identical to {@link Note} but retained at runtime.
 * 
 * @see Note
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.PACKAGE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE })
@Repeatable(RuntimeNotes.class)
@Handler(RuntimeNoteAnnotationHandler.class)
public @interface RuntimeNote {
	/**
	 * A key to an issue tracking system for an issue related to this note.
	 * 
	 * @return Key to an issue tracking system.
	 * @see Note#issue()
	 */
	public String issue() default "";

	/**
	 * A java type which is related to this note.
	 * 
	 * @return A java type related to this note.
	 * @see Note#ref()
	 */
	public Class<?> ref() default NoRef.class;

	/**
	 * Specifies what kind of note this is.
	 * 
	 * @return The kind of note this is.
	 * @see Note#type()
	 */
	public NoteType type() default NoteType.Note;

	/**
	 * A (optional) message which should be reported during compilation.
	 * 
	 * @return The message
	 * @see Note#value()
	 */
	public String value() default "";
}
