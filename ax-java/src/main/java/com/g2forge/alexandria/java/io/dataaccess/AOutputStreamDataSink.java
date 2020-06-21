package com.g2forge.alexandria.java.io.dataaccess;

import java.io.OutputStream;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;

import com.g2forge.alexandria.annotations.note.Note;
import com.g2forge.alexandria.annotations.note.NoteType;
import com.g2forge.alexandria.java.type.ref.ITypeRef;

import lombok.Getter;

@Getter
public abstract class AOutputStreamDataSink implements IDataSink {
	protected boolean used = false;

	@Note(type = NoteType.TODO, value = "Use static type switch", issue = "G2-432")
	@Override
	public <T extends Channel> T getChannel(ITypeRef<T> type) {
		if ((type != null) && !type.isAssignableFrom(ITypeRef.of(WritableByteChannel.class))) throw new IllegalArgumentException();
		@SuppressWarnings("unchecked")
		final T retVal = (T) Channels.newChannel(getStream());
		used = true;
		return retVal;
	}

	@Override
	public Path getPath() {
		throw new UnsupportedOperationException();
	}

	public abstract OutputStream getStream();
}