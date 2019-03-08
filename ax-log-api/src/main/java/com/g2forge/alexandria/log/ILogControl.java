package com.g2forge.alexandria.log;

import org.slf4j.event.Level;

import com.g2forge.alexandria.java.close.ICloseable;

public interface ILogControl {
	public Level getLogLevel();

	public void setLogLevel(Level level);

	public default ICloseable tempLogLevel(Level level) {
		final Level original = getLogLevel();
		setLogLevel(level);
		return () -> setLogLevel(original);
	}
}
