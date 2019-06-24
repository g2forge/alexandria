package com.g2forge.alexandria.annotations.note;

import javax.annotation.processing.ProcessingEnvironment;

public class SourceNoteAnnotationHandler extends ANoteAnnotationHandler<Note> {
	@Override
	protected NoteRecord toRecord(ProcessingEnvironment processingEnvironment, Note annotation) {
		return new NoteRecord(annotation.type(), annotation.value(), annotation.issue(), getRef(processingEnvironment, () -> annotation.ref()));
	}
}
