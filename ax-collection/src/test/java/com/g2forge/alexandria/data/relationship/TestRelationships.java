package com.g2forge.alexandria.data.relationship;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.test.AssertHelpers;

public class TestRelationships {
	@Test
	public void add() {
		final HasScalar scalar = new HasScalar();
		final HasCollection collection = new HasCollection().addScalar(scalar);
		assertRelationship(collection, scalar);
	}

	protected void assertRelationship(final HasCollection collection, final HasScalar scalar) {
		Assert.assertEquals(collection.getScalars(), Arrays.asList(scalar));
		Assert.assertEquals(collection, scalar.getCollection());
		AssertHelpers.assertException(IllegalStateException.class, () -> scalar.setCollection(new HasCollection()));
		AssertHelpers.assertException(IllegalStateException.class, () -> collection.setScalars(new ArrayList<>()));
	}

	@Test
	public void set() {
		final HasCollection collection = new HasCollection();
		final HasScalar scalar = new HasScalar().setCollection(collection);
		assertRelationship(collection, scalar);
	}
}
