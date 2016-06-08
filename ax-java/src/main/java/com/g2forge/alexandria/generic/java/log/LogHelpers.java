package com.g2forge.alexandria.generic.java.log;

import org.slf4j.Logger;

public class LogHelpers {
	public static <T extends Throwable> T log(final Logger logger, final T throwable) {
		logger.error(throwable.getMessage(), throwable);
		return throwable;
	}
}
