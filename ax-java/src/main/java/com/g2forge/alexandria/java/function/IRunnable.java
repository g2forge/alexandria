package com.g2forge.alexandria.java.function;

@FunctionalInterface
public interface IRunnable extends Runnable, IThrowRunnable<RuntimeException> {
	public static IRunnable create(IRunnable runnable) {
		return runnable;
	}
}
