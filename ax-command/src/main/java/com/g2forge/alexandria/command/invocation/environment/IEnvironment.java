package com.g2forge.alexandria.command.invocation.environment;

import java.util.Map;

import com.g2forge.alexandria.java.function.IFunction1;

public interface IEnvironment extends IFunction1<String, String> {
	public Map<String, String> toMap();
}
