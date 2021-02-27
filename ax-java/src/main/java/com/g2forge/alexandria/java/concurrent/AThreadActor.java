package com.g2forge.alexandria.java.concurrent;

import com.g2forge.alexandria.java.close.ICloseable;

import lombok.Getter;

public abstract class AThreadActor implements ICloseable {
	protected enum State {
		Initialized,
		Opened,
		Closed;
	}

	@Getter
	protected volatile transient State state = State.Initialized;

	protected Thread thread = null;

	@Override
	public synchronized void close() {
		final boolean wasOpen = state == State.Opened;
		if (wasOpen) shutdown();
		state = State.Closed;
		if (wasOpen) thread.interrupt();
	}

	public boolean isOpen() {
		return state == State.Opened;
	}

	public synchronized AThreadActor open() {
		if (state != State.Initialized) throw new IllegalStateException();
		thread = new Thread(() -> {
			try {
				run();
			} finally {
				close();
			}
		});
		thread.setName(thread.getName() + " (" + getClass().getSimpleName() + ")");
		thread.start();
		state = State.Opened;
		return this;
	}

	protected abstract void run();

	protected void shutdown() {}
}
