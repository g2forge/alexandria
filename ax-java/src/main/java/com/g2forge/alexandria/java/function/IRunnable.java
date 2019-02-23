package com.g2forge.alexandria.java.function;

import java.util.ArrayList;
import java.util.Collection;

import com.g2forge.alexandria.java.core.error.HError;
import com.g2forge.alexandria.java.core.helpers.HCollection;

@FunctionalInterface
public interface IRunnable extends Runnable, IThrowRunnable<RuntimeException> {
	public static void all(Collection<? extends IRunnable> runnables) {
		final Collection<Throwable> throwables = new ArrayList<>();
		for (IRunnable runnable : runnables) {
			try {
				runnable.run();
			} catch (Throwable throwable) {
				throwables.add(throwable);
			}
		}
		if (!throwables.isEmpty()) throw HError.multithrow("One or more operations failed!", throwables);
	}

	public static void all(IRunnable... runnables) {
		all(HCollection.asList(runnables));
	}

	public static void any(Collection<? extends IRunnable> runnables) {
		final Collection<Throwable> throwables = new ArrayList<>();
		for (IRunnable runnable : runnables) {
			try {
				runnable.run();
				return;
			} catch (Throwable throwable) {
				throwables.add(throwable);
			}
		}
		if (!throwables.isEmpty()) throw HError.multithrow("One or more operations failed!", throwables);
	}

	public static void any(IRunnable... runnables) {
		any(HCollection.asList(runnables));
	}

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
