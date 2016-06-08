package com.g2forge.alexandria.generic.type.environment.implementations;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import com.g2forge.alexandria.adt.collection.CollectionHelpers;
import com.g2forge.alexandria.generic.java.filter.IFilter;
import com.g2forge.alexandria.generic.java.filter.implementations.MemberFilter;
import com.g2forge.alexandria.generic.type.IType;
import com.g2forge.alexandria.generic.type.ITypeVariable;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;

public class TypeEnvironment implements ITypeEnvironment {
	protected static final IFilter<Object> parentFilter = new MemberFilter(null, EmptyTypeEnvironment.create()).inverse();
	
	public static ITypeEnvironment create(final Collection<ITypeEnvironment> parents) {
		if (parents.isEmpty()) return null;
		
		final Collection<ITypeEnvironment> nonNull = CollectionHelpers.filter(parents, parentFilter);
		if (nonNull.isEmpty()) return EmptyTypeEnvironment.create();
		if (nonNull.size() == 1) return CollectionHelpers.getAny(nonNull);
		return new TypeEnvironment(null, nonNull);
	}
	
	public static ITypeEnvironment create(final ITypeEnvironment... parents) {
		return create(Arrays.asList(parents));
	}
	
	public static ITypeEnvironment create(final ITypeEnvironment parent) {
		return parent;
	}
	
	protected final Map<ITypeVariable, IType> map;
	
	protected final Collection<ITypeEnvironment> parents;
	
	public TypeEnvironment(final Map<ITypeVariable, IType> map, final Collection<ITypeEnvironment> parents) {
		this.map = map;
		this.parents = CollectionHelpers.filter(parents, parentFilter);
	}
	
	public TypeEnvironment(final Map<ITypeVariable, IType> map, final ITypeEnvironment... parents) {
		this(map, Arrays.asList(parents));
	}
	
	@Override
	public IType map(final ITypeVariable input) {
		if ((map != null) && map.containsKey(input)) return map.get(input);
		if (parents != null) {
			for (final ITypeEnvironment parent : parents) {
				return parent.map(input);
			}
		}
		return null;
	}
}
