package com.g2forge.alexandria.log;

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.core.marker.Helpers;
import com.g2forge.alexandria.java.enums.EnumException;
import com.g2forge.alexandria.service.BasicServiceLoader;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@Helpers
@UtilityClass
public class HLog {
	@Getter(lazy = true)
	private static final ILogControl logControl = HCollection.getOne(new BasicServiceLoader<ILogControl>(ILogControl.class).load());

	public static void log(Logger logger, Level level, Marker marker, String format, Throwable throwable, Object... arguments) {
		switch (level) {
			case ERROR:
				logger.error(marker, format, arguments, throwable);
				break;
			case WARN:
				logger.warn(marker, format, arguments, throwable);
				break;
			case INFO:
				logger.info(marker, format, arguments, throwable);
				break;
			case DEBUG:
				logger.debug(marker, format, arguments, throwable);
				break;
			case TRACE:
				logger.trace(marker, format, arguments, throwable);
				break;
			default:
				throw new EnumException(Level.class, level);
		}
	}

	public static void log(Logger logger, Level level, String message) {
		switch (level) {
			case ERROR:
				logger.error(message);
				break;
			case WARN:
				logger.warn(message);
				break;
			case INFO:
				logger.info(message);
				break;
			case DEBUG:
				logger.debug(message);
				break;
			case TRACE:
				logger.trace(message);
				break;
			default:
				throw new EnumException(Level.class, level);
		}
	}

	public static <T extends Throwable> T log(final Logger logger, final T throwable) {
		logger.error(throwable.getMessage(), throwable);
		return throwable;
	}
}
