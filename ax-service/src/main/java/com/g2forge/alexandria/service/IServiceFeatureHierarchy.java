package com.g2forge.alexandria.service;

import java.util.Collection;

public interface IServiceFeatureHierarchy<S> {
	/**
	 * Get a list of feature interfaces associated with the service interface <code>S</code>. Note that any interfaces which are the parent of a feature interface, and a child
	 * of the service interface <code>S</code> will also be considered feature interfaces.
	 * 
	 * @return
	 */
	public Collection<Class<? extends S>> getFeatureInterfaces();
}
