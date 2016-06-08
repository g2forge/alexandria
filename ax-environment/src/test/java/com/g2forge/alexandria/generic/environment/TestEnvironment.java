package com.g2forge.alexandria.generic.environment;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.generic.environment.implementations.ConstantEnvReference;
import com.g2forge.alexandria.generic.environment.implementations.EmptyEnvironment;
import com.g2forge.alexandria.generic.environment.implementations.TypedEnvReference;
import com.g2forge.alexandria.generic.environment.implementations.TypedMapEnvironment;

public class TestEnvironment {
	public static String getField0(final IEnvReference<Record<EmptyEnvironment>, EmptyEnvironment> reference) {
		return reference.eval(null).getField0().eval(null);
	}
	
	@Test
	public void testBound() {
		final String string = "foo";
		final Record<EmptyEnvironment> record = new Record<>(ConstantEnvReference.create(string), ConstantEnvReference.create(1));
		final ConstantEnvReference<Record<EmptyEnvironment>, EmptyEnvironment> reference = ConstantEnvReference.create(record);
		Assert.assertEquals(string, getField0(reference));
	}
	
	@Test
	public void testMap() {
		final String string = "foo";
		final TypedMapEnvironment environment = new TypedMapEnvironment();
		environment.put(String.class, string);
		
		final Record<ITypedMapEnvironment> record = new Record<>(new TypedEnvReference<>(String.class), new ConstantEnvReference<>(1));
		Assert.assertEquals(string, record.getField0().eval(environment));
		Assert.assertEquals(string, record.bind(environment).getField0().eval(null));
	}
}
