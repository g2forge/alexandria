package com.g2forge.alexandria.metadata.load;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import com.g2forge.alexandria.annotations.note.Note;
import com.g2forge.alexandria.annotations.note.NoteType;
import com.g2forge.alexandria.metadata.annotation.IJavaAnnotations;
import com.g2forge.alexandria.metadata.annotation.implementations.ElementJavaAnnotations;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Data
@Builder(toBuilder = true)
public class SimpleMetadata implements IAnnotatedMetadata {
	protected final AnnotatedElement element;

	protected final Object value;

	@Getter(lazy = true)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private final IJavaAnnotations annotations = new ElementJavaAnnotations(getElement());

	@Note(type = NoteType.TODO, value = "Implement support for loading instance specific metadata", issue = "G2-469")
	public SimpleMetadata(AnnotatedElement element) {
		this(element, null);
	}

	public SimpleMetadata(AnnotatedElement element, Object value) {
		if ((element == null) && (value != null)) {
			this.element = (value instanceof Annotation) ? ((Annotation) value).annotationType() : value.getClass();
			this.value = value;
		} else {
			this.element = element;
			this.value = value;
		}
	}
}
