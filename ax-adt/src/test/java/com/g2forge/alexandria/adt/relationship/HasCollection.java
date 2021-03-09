package com.g2forge.alexandria.adt.relationship;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class HasCollection {
	protected static final CollectionRelationship<HasCollection, HasScalar, List<HasScalar>> RELATIONSHIP_SCALAR = new CollectionRelationship<>(l -> l.scalars, (l, c) -> l.scalars = c, HasScalar::setCollection, ArrayList::new);

	protected List<HasScalar> scalars;

	public HasCollection addScalar(HasScalar typeParameter) {
		return RELATIONSHIP_SCALAR.add(this, typeParameter);
	}

	public HasCollection setScalars(List<HasScalar> typeParameters) {
		return RELATIONSHIP_SCALAR.set(this, typeParameters);
	}
}
