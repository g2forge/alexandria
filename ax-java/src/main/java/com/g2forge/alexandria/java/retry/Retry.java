package com.g2forge.alexandria.java.retry;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.g2forge.alexandria.java.core.error.HError;
import com.g2forge.alexandria.java.function.IRunnable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Retry implements IRunnable {
	protected final Duration duration;

	protected final Duration pause;

	protected final IRunnable runnable;

	public Retry(Duration duration, IRunnable runnable) {
		this(duration, null, runnable);
	}

	@Override
	public void run() {
		Instant now = Instant.now();
		final Instant start = now, end = start.plus(getDuration());
		final List<Throwable> throwables = new ArrayList<>();
		int attempts = 0;
		do {
			try {
				runnable.run();
				return;
			} catch (Throwable throwable) {
				throwables.add(throwable);
				final Duration pause = getPause();
				if (pause != null) {
					synchronized (this) {
						try {
							wait(pause.toMillis());
						} catch (InterruptedException e) {}
					}
				}
			}
			attempts++;
		} while (end.compareTo(now = Instant.now()) >= 0);
		throw HError.withSuppressed(new RuntimeException(String.format("Gave up after %1$d attempts, ran from %2$s to %3$s, ended at %4$s", attempts, start, end, now)), throwables);
	}
}
