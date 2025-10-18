package com.g2forge.alexandria.workqueue;

import java.util.Collection;
import java.util.LinkedList;

import com.g2forge.alexandria.collection.ICollection;
import com.g2forge.alexandria.java.core.marker.ISingleton;

public class BasicWorkQueueFactory implements ISingleton, IWorkQueueFactory {
	protected static final BasicWorkQueueFactory INSTANCE = new BasicWorkQueueFactory();

	public static IWorkQueueFactory create() {
		return INSTANCE;
	}

	protected BasicWorkQueueFactory() {}

	@Override
	public <T> IWorkQueueHandle run(IWorkQueueHandler<T> handler, ICollection<? extends T> initials) {
		final LinkedList<T> queue = new LinkedList<>();
		final IWorkQueueContext<T> context = new IWorkQueueContext<>() {
			@Override
			public long getApproximateQueueSize() {
				return queue.size();
			}

			@Override
			public void queue(Collection<? extends T> tasks) {
				queue.addAll(tasks);
			}
		};

		context.queue(initials);
		while (!queue.isEmpty()) {
			final T task = queue.remove();
			handler.run(context, task);
		}

		return new IWorkQueueHandle() {
			@Override
			public void await() {}

			@Override
			public void cancel() {
				throw new IllegalStateException("Queue is already empty!");
			}
		};
	}
}
