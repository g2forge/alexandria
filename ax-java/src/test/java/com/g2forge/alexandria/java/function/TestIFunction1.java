package com.g2forge.alexandria.java.function;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.adt.name.IStringNamed;
import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public class TestIFunction1 {
	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	protected static class Example implements IStringNamed {
		protected final String name;
	}

	@Test
	public void liftNull() {
		Assert.assertEquals(HCollection.asList(null, "A"), Stream.of(null, new Example("A")).map(IFunction1.liftNull(IStringNamed::getName)).collect(Collectors.toList()));
		Assert.assertEquals(HCollection.asList("B", "A"), Stream.of(null, new Example("A")).map(IFunction1.liftNull(IStringNamed::getName, "B")).collect(Collectors.toList()));
	}
}
