package com.g2forge.alexandria.java.function.builder;

import com.g2forge.alexandria.java.function.IFunction1;

public interface IExtensibleBuilder<T> extends IBuilder<T> {
	public static class ExtensionUnknownException extends IllegalArgumentException {
		private static final long serialVersionUID = 3828266625784382533L;

		public ExtensionUnknownException() {}

		public ExtensionUnknownException(Class<?> type) {
			this(String.format("Extension type %1$s is not supported!", type));
		}

		public ExtensionUnknownException(String message) {
			super(message);
		}

		public ExtensionUnknownException(String message, Throwable cause) {
			super(message, cause);
		}

		public ExtensionUnknownException(Throwable cause) {
			super(cause);
		}
	}

	public <ET, EB extends IBuilder<ET>> IExtensibleBuilder<T> extend(Class<EB> type, IFunction1<? super EB, ? extends ET> function) throws ExtensionUnknownException;
}
