package com.g2forge.alexandria.generic.type.java.type;

import com.g2forge.alexandria.generic.type.IConcreteType;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.member.IJavaConstructorType;
import com.g2forge.alexandria.generic.type.java.member.IJavaFieldType;
import com.g2forge.alexandria.generic.type.java.member.IJavaMethodType;
import com.g2forge.alexandria.generic.type.java.structure.IJavaClassStructure;

public interface IJavaConcreteType extends IJavaType, IConcreteType, IJavaClassStructure<IJavaClassType, IJavaConcreteType, IJavaFieldType, IJavaMethodType, IJavaConstructorType> {
	@Override
	public IJavaConcreteType eval(ITypeEnvironment environment);

}
