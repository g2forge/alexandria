package com.g2forge.alexandria.java.core.error;

import java.util.stream.Stream;

import com.g2forge.alexandria.java.core.helpers.HCollector;
import com.g2forge.alexandria.java.function.ISupplier;

import lombok.Getter;

public class DependencyNotLoadedError extends LinkageError {
	private static final long serialVersionUID = 2218166938261285007L;

	public static <T> T tryWithModule(ISupplier<? extends T> supplier, String... modules) {
		try {
			return supplier.get();
		} catch (NoClassDefFoundError error) {
			throw new DependencyNotLoadedError(error, modules);
		}
	}

	@Getter
	protected final String[] modules;

	public DependencyNotLoadedError(NoClassDefFoundError error, String... modules) {
		super("An optional (provided or test scope) maven module " + Stream.of(modules).collect(HCollector.joining(", ", ", or ")) + " was not loaded, please add a compile dependency to your project!", (Throwable) error);
		this.modules = modules;
	}
}
