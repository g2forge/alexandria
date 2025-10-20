package com.g2forge.alexandria.java.core.error;

import com.g2forge.alexandria.java.function.ISupplier;

public class DependencyNotLoadedError extends LinkageError {
	private static final long serialVersionUID = 2218166938261285007L;

	public static <T> T tryWithModule(String module, ISupplier<? extends T> supplier) {
		try {
			return supplier.get();
		} catch (NoClassDefFoundError error) {
			throw new DependencyNotLoadedError("gb-csv", error);
		}
	}

	public DependencyNotLoadedError() {}

	public DependencyNotLoadedError(String message) {
		super(message);
	}

	public DependencyNotLoadedError(String module, NoClassDefFoundError error) {
		this("Optional (provided scope) maven module " + module + " was not loaded, please add a compile dependency to your project!", (Throwable) error);
	}
	
	public DependencyNotLoadedError(String message, Throwable cause) {
		super(message, cause);
	}
}

