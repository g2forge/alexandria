package com.g2forge.alexandria.adt.relationship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class HasScalar {
	protected static final ScalarRelationship<HasScalar, HasCollection> RELATIONSHIP_COLLECTION = new ScalarRelationship<>(l -> l.collection, (l, r) -> l.collection = r, HasCollection::addScalar);

	protected HasCollection collection;

	public HasScalar setCollection(HasCollection collection) {
		return RELATIONSHIP_COLLECTION.set(this, collection);
	}
}
