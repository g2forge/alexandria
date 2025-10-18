package com.g2forge.alexandria.workqueue;

import java.util.Collection;

import com.g2forge.alexandria.collection.ICollection;
import com.g2forge.alexandria.java.core.helpers.HCollection;

public interface IWorkQueueContext<T> {
	/**
	 * Get a reasonable approximation of the number of remaining tasks in the queue, generally for reporting progress to the user. Note that the size returned
	 * by this method may be exact for simple queue types, or may be approximate for e.g. multithreaded or distributed work queues.
	 * 
	 * @return A reasonable approximation of the number of remaining tasks in the queue.
	 */
	public long getApproximateQueueSize();

	public void queue(Collection<? extends T> tasks);

	public default void queue(ICollection<? extends T> tasks) {
		queue(tasks.toCollection());
	}

	public default void queue(@SuppressWarnings("unchecked") T... tasks) {
		queue(HCollection.asList(tasks));
	}
}
