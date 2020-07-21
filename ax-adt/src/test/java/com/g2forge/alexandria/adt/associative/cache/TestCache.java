package com.g2forge.alexandria.adt.associative.cache;

import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.adt.identity.IIdentity;
import com.g2forge.alexandria.java.adt.tuple.ITuple2G_;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.tee.RecordingFunction;

public class TestCache {
	protected final RecordingFunction<Integer, Integer> function = new RecordingFunction<>(x -> x + 1);

	@Test
	public void cache() {
		final Cache<Integer, Integer> cache = new Cache<Integer, Integer>(function, new LRUCacheEvictionPolicy<>(1));
		Assert.assertEquals(1, cache.apply(0).intValue());
		Assert.assertEquals(1, cache.apply(0).intValue());
		Assert.assertEquals(HCollection.asList(0), function.getRecord().stream().map(ITuple2G_::get0).collect(Collectors.toList()));
	}

	@Test
	public void evict() {
		final Cache<Integer, Integer> cache = new Cache<Integer, Integer>(function, new LRUCacheEvictionPolicy<>(1));
		Assert.assertEquals(1, cache.apply(0).intValue());
		Assert.assertEquals(2, cache.apply(1).intValue());
		Assert.assertEquals(HCollection.asList(0, 1), function.getRecord().stream().map(ITuple2G_::get0).collect(Collectors.toList()));
		Assert.assertEquals(1, cache.apply(0).intValue());
		Assert.assertEquals(HCollection.asList(0, 1, 0), function.getRecord().stream().map(ITuple2G_::get0).collect(Collectors.toList()));
	}

	@Test
	public void identitySame() {
		final Cache<Integer, Integer> cache = Cache.<Integer, Integer>builder().identity(IIdentity.same()).function(function).policy(new LRUCacheEvictionPolicy<>(2)).build();
		final Integer a = new Integer(0), b = new Integer(0);
		Assert.assertEquals(1, cache.apply(a).intValue());
		Assert.assertEquals(1, cache.apply(b).intValue());
		Assert.assertEquals(HCollection.asList(0, 0), function.getRecord().stream().map(ITuple2G_::get0).collect(Collectors.toList()));
		Assert.assertEquals(1, cache.apply(a).intValue());
		Assert.assertEquals(1, cache.apply(b).intValue());
		Assert.assertEquals(1, cache.apply(new Integer(0)).intValue());
		Assert.assertEquals(HCollection.asList(0, 0, 0), function.getRecord().stream().map(ITuple2G_::get0).collect(Collectors.toList()));
	}

	@Test
	public void identityStandard() {
		final Cache<Integer, Integer> cache = Cache.<Integer, Integer>builder().identity(IIdentity.standard()).function(function).policy(NeverCacheEvictionPolicy.create()).build();
		Assert.assertEquals(1, cache.apply(new Integer(0)).intValue());
		Assert.assertEquals(1, cache.apply(new Integer(0)).intValue());
		Assert.assertEquals(HCollection.asList(0), function.getRecord().stream().map(ITuple2G_::get0).collect(Collectors.toList()));
	}
}
