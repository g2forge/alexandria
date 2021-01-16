package com.g2forge.alexandria.filesystem.memory.file;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NonReadableChannelException;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileSeekableByteChannel implements SeekableByteChannel {
	protected final boolean read;

	protected final boolean write;

	protected final File file;

	protected boolean open = true;

	protected long position = 0;

	protected void assertOpen() throws ClosedChannelException {
		if (!open) throw new ClosedChannelException();
	}

	protected void assertRead() throws ClosedChannelException {
		assertOpen();
		if (!read) throw new NonReadableChannelException();
	}

	protected void assertWrite() throws ClosedChannelException {
		assertOpen();
		if (!write) throw new NonWritableChannelException();
	}

	@Override
	public void close() throws IOException {
		open = false;
	}

	@Override
	public boolean isOpen() {
		return open;
	}

	@Override
	public long position() throws IOException {
		return position;
	}

	@Override
	public SeekableByteChannel position(long newPosition) throws IOException {
		if ((newPosition < 0) || (newPosition > Integer.MAX_VALUE)) throw new IllegalArgumentException();
		assertOpen();
		this.position = newPosition;
		return this;
	}

	@Override
	public int read(ByteBuffer dst) throws IOException {
		assertRead();
		synchronized (file) {
			final byte[] data = file.getData();
			if (data.length == position) {
				file.getBasicAttributes().access(null);
				return -1;
			}

			final int retVal = (int) Math.min(dst.remaining(), data.length - position);
			dst.put(data, (int) position, retVal);
			position += retVal;

			file.getBasicAttributes().access(null);
			return retVal;
		}
	}

	@Override
	public long size() throws IOException {
		synchronized (file) {
			return file.getData().length;
		}
	}

	@Override
	public SeekableByteChannel truncate(long size) throws IOException {
		assertWrite();
		synchronized (file) {
			if (size() > size) {
				file.setData(Arrays.copyOfRange(file.getData(), 0, (int) size));
				if (position > size) position = size;
				file.getBasicAttributes().modify(null);
			}
		}
		return this;
	}

	@Override
	public int write(ByteBuffer src) throws IOException {
		if (position > Integer.MAX_VALUE) throw new IllegalStateException();
		long newLength = position + src.remaining();
		if (newLength > Integer.MAX_VALUE) newLength = Integer.MAX_VALUE;

		assertWrite();
		synchronized (file) {
			byte[] data = file.getData();
			if (data.length < newLength) file.setData(data = Arrays.copyOf(data, (int) newLength));
			final int retVal = (int) (newLength - position);
			src.get(data, (int) position, retVal);
			position += retVal;

			file.getBasicAttributes().modify(null);
			return retVal;
		}
	}
}