package com.g2forge.alexandria.java.function;

@FunctionalInterface
public interface IThrowRunnable<T extends Throwable> extends IFunctional {
	public static <T extends Throwable> IThrowRunnable<T> create(IThrowRunnable<T> runnable) {
		return runnable;
	}

	public void run() throws T;

	public default IThrowRunnable<T> wrap(IRunnable pre, IRunnable post) {
		return () -> {
			pre.run();
			try {
				run();
			} finally {
				post.run();
			}
		};
	}
}
