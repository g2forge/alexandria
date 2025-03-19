package com.g2forge.alexandria.java.close;

import java.io.PrintStream;

import com.g2forge.alexandria.java.core.error.HError;

public abstract class AGuaranteeClose implements ICloseable {
	private transient volatile Thread thread;

	public AGuaranteeClose() {
		this(true);
	}

	public AGuaranteeClose(boolean autoclose) {
		if (autoclose) {
			this.thread = new Thread() {
				public void run() {
					thread = null;
					try {
						close();
					} catch (Throwable throwable) {
						final PrintStream shutdownErrorStream = System.err;
						if (shutdownErrorStream != null) {
							shutdownErrorStream.println("Exception in thread \"" + Thread.currentThread().getName() + "\" created to guarantee close of \"" + AGuaranteeClose.this + "\"");
							throwable.printStackTrace(shutdownErrorStream);
						} else HError.throwQuietly(throwable);
					}
				}
			};
			Runtime.getRuntime().addShutdownHook(thread);
		} else thread = null;
	}

	@Override
	public void close() {
		try {
			if (thread != null) Runtime.getRuntime().removeShutdownHook(thread);
		} finally {
			thread = null;
			closeInternal();
		}
	}

	protected abstract void closeInternal();
}
