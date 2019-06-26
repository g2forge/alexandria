package com.g2forge.alexandria.annotations.note;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.g2forge.alexandria.annotations.Handler;

@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.PACKAGE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE })
@Repeatable(Notes.class)
@Handler(value = SourceNoteAnnotationHandler.class, options = Note.IssueFormat.KEY)
public @interface Note {
	public class IssueFormat {
		public static final String KEY = "issueFormat";

		public static final String DEFAULT = "%1$s";
	}
	
	/**
	 * A marker interface which can be used with {@link Note#ref()} to indicate that there's no reference.
	 */
	public interface NoRef {}

	/**
	 * A key to an issue tracking system for an issue related to this note. For example could be the Jira issue key.
	 * 
	 * @return Key to an issue tracking system.
	 */
	public String issue() default "";

	/**
	 * A java type which is related to this note. This may be used, with a marker interface, to tie together several related notes in your code base.
	 * 
	 * @return A java type related to this note.
	 */
	public Class<?> ref() default NoRef.class;

	/**
	 * Specifies what kind of note this is. See {@link NoteType} for various options like {@link NoteType#TODO}. Of course the default is {@link NoteType#Note}.
	 * 
	 * @return The kind of note this is.
	 */
	public NoteType type() default NoteType.Note;

	/**
	 * A (optional) message which should be reported during compilation. You may include format specifiers for the following information:
	 * 
	 * <ul>
	 * <li><code>%1$s</code>: Element; the element which was annotated.</li>
	 * <li><code>%2$s</code>: Type; the {@link #type()} of this note</li>
	 * <li><code>%3$s</code>: Issue; the {@link #issue()}</li>
	 * <li><code>%4$s</code>: Ref (qualified); the fully qualified name of the {@link #ref()}</li>
	 * <li><code>%5$s</code>: Ref (simple); the single name of the {@link #ref()}</li>
	 * </ul>
	 * 
	 * Note that any of these values left out of your custom message will still be displayed in a standard format. These format specifiers exist to give you
	 * control over how the information is displayed.
	 * 
	 * @return The message format string.
	 */
	public String value() default "";
}
