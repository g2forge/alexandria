package com.g2forge.alexandria.generic.environment.implementations;

import com.g2forge.alexandria.generic.environment.IEnvironment;

public class EmptyEnvironment implements IEnvironment {
	protected static final EmptyEnvironment singleton = new EmptyEnvironment();
	
	public static EmptyEnvironment create() {
		return singleton;
	}
	
	private EmptyEnvironment() {}
}
