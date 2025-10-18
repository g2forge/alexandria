package com.g2forge.alexandria.workqueue;

public interface IWorkQueueHandle {
	public void await();

	public void cancel();
}
