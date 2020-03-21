package com.g2forge.alexandria.java.reflect.accessor;

import java.lang.reflect.Type;

import com.g2forge.alexandria.java.core.helpers.HArray;
import com.g2forge.alexandria.java.text.HString;

public enum JavaAccessorType {
	GET {
		@Override
		public boolean isMatchingParameterTypes(Type[] types) {
			return types.length == 0;
		}

		@Override
		public boolean isMatchingReturnType(Type type) {
			return !Void.TYPE.equals(type);
		}
	},
	IS {
		@Override
		public boolean isMatchingParameterTypes(Type[] types) {
			return types.length == 0;
		}

		@Override
		public boolean isMatchingReturnType(Type type) {
			return Boolean.TYPE.equals(type) || Boolean.class.equals(type);
		}
	},
	SET {
		@Override
		public boolean isMatchingParameterTypes(Type[] types) {
			return types.length == 1;
		}

		@Override
		public boolean isMatchingReturnType(Type type) {
			return true;
		}
	};

	public static final String[] PREFIXES_ARRAY = HArray.map(String.class, JavaAccessorType::getPrefix, JavaAccessorType.values());

	public static String getFieldName(String methodName) {
		final String stripped = HString.stripPrefix(methodName, JavaAccessorType.PREFIXES_ARRAY);
		if (stripped == methodName) return null;
		return HString.lowercase(stripped);
	}

	public String getPrefix() {
		return name().toLowerCase();
	}

	public boolean isMatchingName(String name) {
		return name.startsWith(getPrefix());
	}

	public abstract boolean isMatchingParameterTypes(Type[] types);

	public abstract boolean isMatchingReturnType(Type type);
}