package com.g2forge.alexandria.analysis;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;

import com.g2forge.alexandria.java.type.ref.ITypeRef;

public interface IMethodAnalyzer {
	public org.apache.bcel.classfile.Method getBCEL();

	public default Constructor<?> getConstructor() {
		if (getExecutable() instanceof Constructor) return (Constructor<?>) getExecutable();
		throw new UnsupportedOperationException();
	}

	public Executable getExecutable();

	public default Method getMethod() {
		if (getExecutable() instanceof Method) return (Method) getExecutable();
		throw new UnsupportedOperationException();
	}

	public String getPath();

	public default ITypeRef<?> getReturnType() {
		final Executable executable = getExecutable();
		if (executable instanceof Method) return ITypeRef.of(((Method) executable).getReturnType());
		if (executable instanceof Constructor) return ITypeRef.of(((Constructor<?>) executable).getDeclaringClass());
		throw new UnsupportedOperationException();
	}
}
