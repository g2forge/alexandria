package com.g2forge.alexandria.java.io.dataaccess;

import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;

import com.g2forge.alexandria.annotations.note.Note;
import com.g2forge.alexandria.annotations.note.NoteType;
import com.g2forge.alexandria.java.core.resource.IResource;
import com.g2forge.alexandria.java.type.ref.ITypeRef;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResourceDataSource implements INamedDataSource {
	protected final IResource resource;

	@Note(type = NoteType.TODO, value = "Use static type switch", issue = "G2-432")
	@Override
	public <T extends Channel> T getChannel(ITypeRef<T> type) {
		if ((type != null) && !type.isAssignableFrom(ITypeRef.of(ReadableByteChannel.class))) throw new IllegalArgumentException();
		@SuppressWarnings("unchecked")
		final T retVal = (T) Channels.newChannel(getResource().getResourceAsStream(true));
		return retVal;
	}

	@Override
	public String getName() {
		return getResource().getResource();
	}

	@Override
	public Path getPath() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isUsed() {
		return false;
	}
}