package com.g2forge.alexandria.analysis;

import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;

import com.g2forge.alexandria.java.core.helpers.HArray;

public class HBCEL {
	public static java.lang.reflect.Type getType(Type type) {
		if (type instanceof ObjectType) {
			try {
				return HBCEL.class.getClass().getClassLoader().loadClass(((ObjectType) type).getClassName());
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		if (type instanceof BasicType) {
			if (type == BasicType.VOID) return Void.TYPE;
			if (type == BasicType.BOOLEAN) return Boolean.TYPE;
			if (type == BasicType.BYTE) return Byte.TYPE;
			if (type == BasicType.SHORT) return Short.TYPE;
			if (type == BasicType.INT) return Integer.TYPE;
			if (type == BasicType.LONG) return Long.TYPE;
			if (type == BasicType.FLOAT) return Float.TYPE;
			if (type == BasicType.DOUBLE) return Double.TYPE;
			if (type == BasicType.OBJECT) return Object.class;
		}
		throw new IllegalArgumentException();
	}

	public static java.lang.reflect.Type[] getTypes(Type... types) {
		return HArray.map(java.lang.reflect.Type.class, HBCEL::getType, types);
	}
}
