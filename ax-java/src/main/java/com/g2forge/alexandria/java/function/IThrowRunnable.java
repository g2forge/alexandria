package com.g2forge.alexandria.java.function;

@FunctionalInterface
public interface IThrowRunnable<T extends Throwable> {
	public static <T extends Throwable> IThrowRunnable<T> create(IThrowRunnable<T> runnable) {
		return runnable;
	}

	public void run() throws T;
}
