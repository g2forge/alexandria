package com.g2forge.alexandria.service;

import java.util.ServiceConfigurationError;

import lombok.Getter;

public class SmartServiceConfigurationError extends ServiceConfigurationError {
	private static final long serialVersionUID = -4456103999698974186L;

	@Getter
	protected final Class<?> key;

	public SmartServiceConfigurationError(Class<?> key, String msg) {
		super(key.getName() + ": " + msg);
		this.key = key;
	}

	public SmartServiceConfigurationError(Class<?> key, String msg, Throwable cause) {
		super(key.getName() + ": " + msg, cause);
		this.key = key;
	}
}
