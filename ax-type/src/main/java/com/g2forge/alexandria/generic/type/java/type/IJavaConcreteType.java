package com.g2forge.alexandria.generic.type.java.type;

import java.util.List;

import com.g2forge.alexandria.generic.type.IBoundType;
import com.g2forge.alexandria.generic.type.IConcreteType;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.member.IJavaConstructorType;
import com.g2forge.alexandria.generic.type.java.member.IJavaFieldType;
import com.g2forge.alexandria.generic.type.java.member.IJavaMethodType;
import com.g2forge.alexandria.generic.type.java.structure.IJavaClassStructure;

public interface IJavaConcreteType extends IJavaType, IConcreteType, IBoundType, IJavaClassStructure<IJavaClassType, IJavaConcreteType, IJavaFieldType, IJavaMethodType, IJavaConstructorType> {
	@Override
	public IJavaClassType erase();

	@Override
	public IJavaConcreteType eval(ITypeEnvironment environment);

	@Override
	public List<? extends IJavaType> getActuals();

	public IJavaType getOwner();

	public IJavaType getRaw();

	public ITypeEnvironment toEnvironment();

	public IJavaConcreteType toNonPrimitive();
}
