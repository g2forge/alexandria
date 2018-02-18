package com.g2forge.alexandria.log;

import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import com.g2forge.alexandria.annotations.service.Service;
import com.g2forge.alexandria.java.enums.EnumException;

import ch.qos.logback.classic.Logger;

@Service(ILogControl.class)
public class LogbackControl implements ILogControl {
	@Override
	public void setLogLevel(Level level) {
		((Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(translate(level));
	}

	public ch.qos.logback.classic.Level translate(Level level) {
		switch (level) {
			case ERROR:
				return ch.qos.logback.classic.Level.ERROR;
			case WARN:
				return ch.qos.logback.classic.Level.WARN;
			case INFO:
				return ch.qos.logback.classic.Level.INFO;
			case DEBUG:
				return ch.qos.logback.classic.Level.DEBUG;
			case TRACE:
				return ch.qos.logback.classic.Level.TRACE;
			default:
				throw new EnumException(Level.class, level);
		}
	}
}