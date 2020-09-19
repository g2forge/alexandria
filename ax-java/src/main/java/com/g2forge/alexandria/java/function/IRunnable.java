package com.g2forge.alexandria.java.function;

@FunctionalInterface
public interface IRunnable extends Runnable, IThrowRunnable<RuntimeException> {
	public static IRunnable create(IRunnable runnable) {
		return runnable;
	}

	public static IRunnable nop() {
		return () -> {};
	}

	public default IRunnable wrap(IRunnable pre, IRunnable post) {
		return () -> {
			if (pre != null) pre.run();
			try {
				run();
			} finally {
				if (post != null) post.run();
			}
		};
	}
}
