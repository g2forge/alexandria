package com.g2forge.alexandria.generic.reflection.record.reflected.implementations;

import java.util.Collection;
import java.util.Collections;

import com.g2forge.alexandria.adt.collection.CollectionHelpers;
import com.g2forge.alexandria.generic.java.delayed.implementations.ALateBound;
import com.g2forge.alexandria.generic.java.map.IMap1;
import com.g2forge.alexandria.generic.reflection.object.IJavaClassReflection;
import com.g2forge.alexandria.generic.reflection.object.IJavaFieldReflection;
import com.g2forge.alexandria.generic.reflection.object.implementations.JavaClassReflection;
import com.g2forge.alexandria.generic.reflection.record.reflected.IReflectedFieldType;
import com.g2forge.alexandria.generic.reflection.record.reflected.IReflectedRecordType;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.generic.type.java.IJavaClassType;
import com.g2forge.alexandria.generic.type.java.structure.JavaMembership;
import com.g2forge.alexandria.java.tuple.ITuple1G_;

public class ReflectedRecordType<R> implements IReflectedRecordType<R> {
	protected final IJavaClassReflection<R> reflection;
	
	protected final ITuple1G_<Collection<IReflectedFieldType<R, ?>>> fields = new ALateBound<Collection<IReflectedFieldType<R, ?>>>() {
		@Override
		protected Collection<IReflectedFieldType<R, ?>> compute() {
			return Collections.unmodifiableCollection(CollectionHelpers.map(new IMap1<IJavaFieldReflection<R, ?>, IReflectedFieldType<R, ?>>() {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				@Override
				public IReflectedFieldType<R, ?> map(IJavaFieldReflection<R, ?> input) {
					return new ReflectedFieldType(input);
				}
			}, reflection.getFields(JavaMembership.All)));
		}
	};
	
	/**
	 * @param type
	 */
	public ReflectedRecordType(final Class<R> type, final ITypeEnvironment environment) {
		this(new JavaClassReflection<R>(type, environment));
	}
	
	/**
	 * @param reflection
	 */
	public ReflectedRecordType(IJavaClassReflection<R> reflection) {
		this.reflection = reflection;
	}
	
	@Override
	public R create() {
		return reflection.newInstance();
	}
	
	@Override
	public Collection<? extends IReflectedFieldType<R, ?>> getFields() {
		return fields.get0();
	}
	
	@Override
	public IJavaClassType getType() {
		return reflection.getType();
	}
}
