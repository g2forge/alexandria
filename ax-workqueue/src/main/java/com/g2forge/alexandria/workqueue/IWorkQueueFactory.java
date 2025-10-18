package com.g2forge.alexandria.workqueue;

import com.g2forge.alexandria.collection.CollectionCollection;
import com.g2forge.alexandria.collection.ICollection;

public interface IWorkQueueFactory {
	public <T> IWorkQueueHandle run(IWorkQueueHandler<T> handler, ICollection<? extends T> initials);

	public default <T> IWorkQueueHandle run(IWorkQueueHandler<T> handler, @SuppressWarnings("unchecked") T... initials) {
		return run(handler, new CollectionCollection<>(initials));
	}
}
