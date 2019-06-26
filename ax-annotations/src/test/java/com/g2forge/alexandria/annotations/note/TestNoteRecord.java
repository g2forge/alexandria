package com.g2forge.alexandria.annotations.note;

import org.junit.Assert;
import org.junit.Test;

public class TestNoteRecord {
	@Test
	public void string() {
		Assert.assertEquals("Note: This is a note", new NoteRecord(NoteType.Note, "This is a note", null, null).toString(Note.IssueFormat.DEFAULT, null));
		Assert.assertEquals("TODO (I have issues): This has issues", new NoteRecord(NoteType.TODO, "This has issues", "I have issues", null).toString(Note.IssueFormat.DEFAULT, null));
		Assert.assertEquals("Request for proposal: Please suggest something (see com.g2forge.alexandria.annotations.note.TestNoteRecord)", new NoteRecord(NoteType.RFP, "Please suggest something", null, new ANoteAnnotationHandler.ClassType(TestNoteRecord.class)).toString(Note.IssueFormat.DEFAULT, null));
		Assert.assertEquals("Hack (I'll follow up, see com.g2forge.alexandria.annotations.note.TestNoteRecord)", new NoteRecord(NoteType.Hack, "", "I'll follow up", new ANoteAnnotationHandler.ClassType(TestNoteRecord.class)).toString(Note.IssueFormat.DEFAULT, null));
		Assert.assertEquals("Request for comment (I'll follow up): Did I do this right? (see com.g2forge.alexandria.annotations.note.TestNoteRecord)", new NoteRecord(NoteType.RFC, "Did I do this right?", "I'll follow up", new ANoteAnnotationHandler.ClassType(TestNoteRecord.class)).toString(Note.IssueFormat.DEFAULT, null));
	}

	@Test
	public void message() {
		Assert.assertEquals("Whatever Note", new NoteRecord(NoteType.Note, "Whatever %2$s", null, null).toString(Note.IssueFormat.DEFAULT, null));
		Assert.assertEquals("Note: Whatever a", new NoteRecord(NoteType.Note, "Whatever %3$s", "a", null).toString(Note.IssueFormat.DEFAULT, null));
		Assert.assertEquals("Note (a): This is somehow related to com.g2forge.alexandria.annotations.note.TestNoteRecord", new NoteRecord(NoteType.Note, "This is somehow related to %4$s", "a", new ANoteAnnotationHandler.ClassType(TestNoteRecord.class)).toString(Note.IssueFormat.DEFAULT, null));
		Assert.assertEquals("Note (a): This is somehow related to TestNoteRecord", new NoteRecord(NoteType.Note, "This is somehow related to %5$s", "a", new ANoteAnnotationHandler.ClassType(TestNoteRecord.class)).toString(Note.IssueFormat.DEFAULT, null));
	}
}
