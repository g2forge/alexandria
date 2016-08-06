package com.g2forge.alexandria.reflection.record.v2;

import com.g2forge.alexandria.reflection.annotations.IJavaAnnotations;
import com.g2forge.alexandria.reflection.object.IJavaTypeReflection;

public interface IPropertyType<P> {
	public IJavaAnnotations getAnnotations();

	public String getName();

	public IJavaTypeReflection<P> getType();

	public P getValue(Object object);

	public void setValue(Object object, P value);
}
