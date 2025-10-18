package com.g2forge.alexandria.workqueue;

@FunctionalInterface
public interface IWorkQueueHandler<T> {
	public void run(IWorkQueueContext<T> context, T task);
}
