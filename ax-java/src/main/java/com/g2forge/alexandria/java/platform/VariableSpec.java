package com.g2forge.alexandria.java.platform;

public enum VariableSpec {
	POSIX {
		@Override
		public String createString(boolean local, String name) {
			return "${" + name + "}";
		}
	},
	CMD {
		@Override
		public String createString(boolean local, String name) {
			return "%" + name + "%";
		}
	},
	POWERSHELL {
		@Override
		public String createString(boolean local, String name) {
			return "${" + (local ? "" : "env:") + name + "}";
		}
	};

	public abstract String createString(boolean local, String name);
}
