package com.g2forge.alexandria.java.reflect.accessor;

import java.lang.reflect.Type;

import com.g2forge.alexandria.java.text.HString;

public interface IJavaAccessorMethod {
	public default JavaAccessorType getAccessorType() {
		final String name = getName();
		final Type[] parameterTypes = getParameterTypes();
		final Type returnType = getReturnType();
		for (JavaAccessorType type : JavaAccessorType.values()) {
			if (type.isMatchingName(name) && type.isMatchingReturnType(returnType) && type.isMatchingParameterTypes(parameterTypes)) return type;
		}
		return null;
	}

	public default String getFieldName() {
		final String name = getName();
		final String stripped = HString.stripPrefix(name, JavaAccessorType.PREFIXES_ARRAY);
		// Disabled to support accessors without prefixes
		// if (stripped == name) return null;
		if (stripped.isEmpty()) return "<unnammed>";
		return HString.lowercase(stripped);
	}

	public String getName();

	public Type[] getParameterTypes();

	public Type getReturnType();
}
