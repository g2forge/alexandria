package com.g2forge.alexandria.service;

import java.util.Collection;

public interface IServiceFeatureHierarchy<S> {
	/**
	 * Get all the feature interfaces associated with the service interface <code>S</code>. Note that any interfaces which are the parent of a feature
	 * interface, and a child of the service interface <code>S</code> will also be considered feature interfaces.
	 * 
	 * @return The feature interfaces associated with service interface <code>S</code>.
	 */
	public Collection<Class<? extends S>> getFeatureInterfaces();
}
