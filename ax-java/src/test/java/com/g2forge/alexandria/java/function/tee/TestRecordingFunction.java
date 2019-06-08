package com.g2forge.alexandria.java.function.tee;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.adt.tuple.implementations.Tuple2GSO;
import com.g2forge.alexandria.java.core.helpers.HCollection;

public class TestRecordingFunction {
	@Test
	public void test() {
		final RecordingFunction<Integer, Integer> function = new RecordingFunction<>(x -> x + 1);
		Assert.assertEquals(Integer.valueOf(1), function.apply(0));
		Assert.assertEquals(HCollection.asList(new Tuple2GSO<>(0, 1)), function.getRecord());
	}
}
