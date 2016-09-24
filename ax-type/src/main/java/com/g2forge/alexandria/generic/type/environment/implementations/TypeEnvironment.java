package com.g2forge.alexandria.generic.type.environment.implementations;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

import com.g2forge.alexandria.generic.type.IType;
import com.g2forge.alexandria.generic.type.IVariableType;
import com.g2forge.alexandria.generic.type.environment.ITypeEnvironment;
import com.g2forge.alexandria.java.core.helpers.HCollection;

public class TypeEnvironment implements ITypeEnvironment {
	protected static final Predicate<Object> PARENT_PREDICATE = ((Predicate<Object>) HCollection.asSet(null, EmptyTypeEnvironment.create())::contains).negate();

	public static ITypeEnvironment create(final ITypeEnvironment parent) {
		return parent;
	}

	public static ITypeEnvironment create(Map<IVariableType, IType> map, final Collection<ITypeEnvironment> parents) {
		final boolean noMap = (map == null) || map.isEmpty();
		if (noMap && parents.isEmpty()) return null;

		final Collection<ITypeEnvironment> nonEmpty = HCollection.filter(parents, PARENT_PREDICATE);
		if (noMap) {
			if (nonEmpty.isEmpty()) return EmptyTypeEnvironment.create();
			if (nonEmpty.size() == 1) return HCollection.getAny(nonEmpty);
		}
		return new TypeEnvironment(map, nonEmpty);
	}

	public static ITypeEnvironment create(Map<IVariableType, IType> map, final ITypeEnvironment... parents) {
		return create(map, HCollection.asList(parents));
	}

	protected final Map<IVariableType, IType> map;

	protected final Collection<ITypeEnvironment> parents;

	public TypeEnvironment(final Map<IVariableType, IType> map, final Collection<ITypeEnvironment> parents) {
		this.map = map;
		final Collection<ITypeEnvironment> filtered = HCollection.filter(parents, PARENT_PREDICATE);
		this.parents = filtered.isEmpty() ? null : filtered;
	}

	public TypeEnvironment(final Map<IVariableType, IType> map, final ITypeEnvironment... parents) {
		this(map, HCollection.asList(parents));
	}

	@Override
	public IType apply(final IVariableType input) {
		if ((map != null) && map.containsKey(input)) return map.get(input);
		if (parents != null) for (final ITypeEnvironment parent : parents) {
			final IType retVal = parent.apply(input);
			if (retVal != null) return retVal;
		}
		return null;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + System.identityHashCode(this);
	}
}
