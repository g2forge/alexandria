package com.g2forge.alexandria.annotations.note;

import javax.annotation.processing.ProcessingEnvironment;

public class RuntimeNoteAnnotationHandler extends ANoteAnnotationHandler<RuntimeNote> {
	@Override
	protected NoteRecord toRecord(ProcessingEnvironment processingEnvironment, RuntimeNote annotation) {
		return new NoteRecord(annotation.type(), annotation.value(), annotation.issue(), getRef(processingEnvironment, () -> annotation.ref()));
	}
}
