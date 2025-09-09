package com.g2forge.alexandria.log;

import java.util.List;
import java.util.Queue;

import org.slf4j.Marker;
import org.slf4j.event.KeyValuePair;
import org.slf4j.event.Level;
import org.slf4j.event.LoggingEvent;
import org.slf4j.helpers.LegacyAbstractLogger;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.log.CollectionLogger.CollectionLoggerEvent.CollectionLoggerEventBuilder;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@RequiredArgsConstructor
@Getter(AccessLevel.PUBLIC)
public class CollectionLogger extends LegacyAbstractLogger {
	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	public static class CollectionLoggerEvent implements LoggingEvent {
		protected final Level level;

		@Singular
		protected final List<Marker> markers;

		protected final String loggerName;

		protected final String threadName;

		protected final String message;

		protected final Object[] argumentArray;

		protected final long timeStamp;

		protected final Throwable throwable;

		@Override
		public List<Object> getArguments() {
			return HCollection.asList(getArgumentArray());
		}

		@Override
		public List<KeyValuePair> getKeyValuePairs() {
			return HCollection.emptyList();
		}
	}

	private static final long serialVersionUID = 331049264414397420L;

	protected static final boolean ENABLE_ALL = true;

	protected final String name;

	protected final Queue<CollectionLoggerEvent> queue;

	@Override
	protected String getFullyQualifiedCallerName() {
		return null;
	}

	@Override
	protected void handleNormalizedLoggingCall(Level level, Marker marker, String message, Object[] arguments, Throwable throwable) {
		final CollectionLoggerEventBuilder builder = CollectionLoggerEvent.builder();
		builder.timeStamp(System.currentTimeMillis());
		builder.level(level);
		builder.loggerName(getName());
		if (marker != null) builder.marker(marker);
		builder.message(message);
		builder.threadName(Thread.currentThread().getName());
		builder.argumentArray(arguments);
		builder.throwable(throwable);
		getQueue().add(builder.build());
	}

	@Override
	public boolean isDebugEnabled() {
		return ENABLE_ALL;
	}

	@Override
	public boolean isErrorEnabled() {
		return ENABLE_ALL;
	}

	@Override
	public boolean isInfoEnabled() {
		return ENABLE_ALL;
	}

	@Override
	public boolean isTraceEnabled() {
		return ENABLE_ALL;
	}

	public boolean isWarnEnabled() {
		return ENABLE_ALL;
	}
}
