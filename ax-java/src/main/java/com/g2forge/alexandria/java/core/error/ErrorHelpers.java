package com.g2forge.alexandria.java.core.error;

public class ErrorHelpers {
	public static RuntimeException multithrow(String message, Iterable<? extends Throwable> throwables) {
		final RuntimeException retVal = new RuntimeException(message);
		for (Throwable throwable : throwables)
			retVal.addSuppressed(throwable);
		return retVal;
	}

	public static RuntimeException multithrow(String message, Throwable... throwables) {
		final RuntimeException retVal = new RuntimeException(message);
		for (Throwable throwable : throwables)
			retVal.addSuppressed(throwable);
		return retVal;
	}
}
