package com.g2forge.alexandria.java.core.iface;

/**
 * Represents a class which can be checked, generally for consistency or internal state errors.
 */
@FunctionalInterface
public interface ICheckable {
	/**
	 * Perform the check. This method may handle errors by correcting them, logging them or throwing an exception.
	 */
	public void check();
}
