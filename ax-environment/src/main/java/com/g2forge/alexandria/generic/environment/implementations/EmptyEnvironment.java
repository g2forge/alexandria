package com.g2forge.alexandria.generic.environment.implementations;

import com.g2forge.alexandria.generic.environment.IEnvironment;
import com.g2forge.alexandria.java.core.iface.ISingleton;

public class EmptyEnvironment implements IEnvironment, ISingleton {
	protected static final EmptyEnvironment singleton = new EmptyEnvironment();
	
	public static EmptyEnvironment create() {
		return singleton;
	}
	
	private EmptyEnvironment() {}
}
