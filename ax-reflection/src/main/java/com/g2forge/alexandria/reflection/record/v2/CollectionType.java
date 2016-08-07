package com.g2forge.alexandria.reflection.record.v2;

import java.util.Collection;

import com.g2forge.alexandria.generic.type.java.JavaTypeHelpers;
import com.g2forge.alexandria.generic.type.java.type.IJavaConcreteType;
import com.g2forge.alexandria.generic.type.java.type.IJavaType;

import lombok.Getter;

@Getter
public class CollectionType {
	protected final IJavaConcreteType collection;

	protected final IJavaConcreteType element;

	public CollectionType(IJavaType type) {
		final IJavaConcreteType concrete = type.resolve();
		if (Collection.class.isAssignableFrom((Class<?>) concrete.erase().getJavaType())) {
			collection = concrete.getParent(JavaTypeHelpers.toType(Collection.class, null));
			element = collection.getActuals().get(0).eval(collection.toEnvironment()).resolve();
		} else {
			collection = null;
			element = concrete;
		}
	}
}
