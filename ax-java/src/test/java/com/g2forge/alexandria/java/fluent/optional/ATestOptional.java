package com.g2forge.alexandria.java.fluent.optional;

import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.fluent.optional.factory.IOptionalFactory;

public abstract class ATestOptional {
	public static class Dummy {}

	@Test
	public void empty() {
		final IOptional<Object> optional = getFactory().empty();
		Assert.assertFalse(optional.isNotEmpty());
		Assert.assertTrue(optional.isEmpty());
		try {
			optional.get();
			Assert.fail("get on an empty optional should throw an exception!");
		} catch (NoSuchElementException exception) {}
	}

	@Test
	public void equals() {
		Assert.assertEquals(getFactory().of(1), getFactory().of(1));
		Assert.assertEquals(getFactory().empty(), getFactory().empty());
		Assert.assertNotEquals(getFactory().of(1), getFactory().of(2));
		Assert.assertNotEquals(getFactory().of(1), getFactory().empty());
	}

	@Test
	public void fallbackEmpty() {
		final Object value = new Object();
		Assert.assertSame(value, getFactory().empty().fallback(getFactory().of(value)).get());
	}

	@Test
	public void fallbackValue() {
		final Object value = new Object();
		Assert.assertSame(value, getFactory().of(value).fallback(getFactory().of(new Object())).get());
	}

	@Test
	public void filterFail() {
		Assert.assertFalse(getFactory().of('a').filter(Character::isUpperCase).isNotEmpty());
	}

	@Test
	public void filterPass() {
		Assert.assertTrue(getFactory().of('a').filter(Character::isLowerCase).isNotEmpty());
	}

	@Test
	public void flatmapEmpty() {
		Assert.assertTrue(getFactory().<Integer>empty().flatMap(i -> (i & 0x1) != 0 ? getFactory().of(i + 1) : getFactory().empty()).isEmpty());
	}

	@Test(expected = NullPointerException.class)
	public void flatmapNull() {
		getFactory().empty().flatMap(null);
	}

	@Test
	public void flatmapValueEmpty() {
		Assert.assertTrue(getFactory().of(2).flatMap(i -> (i & 0x1) != 0 ? getFactory().of(i + 1) : getFactory().empty()).isEmpty());
	}

	@Test
	public void flatmapValueValue() {
		Assert.assertEquals(2, getFactory().of(1).flatMap(i -> (i & 0x1) != 0 ? getFactory().of(i + 1) : getFactory().empty()).get().intValue());
	}

	protected abstract IOptionalFactory getFactory();

	@Test
	public void hashcode() {
		final Object value = new Object();
		Assert.assertEquals(value.hashCode(), getFactory().of(value).hashCode());
		Assert.assertEquals(Boolean.TRUE.hashCode(), getFactory().empty().hashCode());
	}

	protected boolean isAllowNull() {
		return !getFactory().ofNullable(null).isEmpty();
	}

	@Test
	public void mapEmpty() {
		Assert.assertTrue(getFactory().<Integer>empty().map(i -> i + 1).isEmpty());
	}

	@Test(expected = NullPointerException.class)
	public void mapNull() {
		getFactory().empty().map(null);
	}

	@Test
	public void mapValue() {
		Assert.assertEquals(2, getFactory().of(1).map(i -> i + 1).get().intValue());
	}

	@Test
	public void ofNullable() {
		Assert.assertTrue(getFactory().ofNullable(null).isEmpty());
	}

	@Test
	public void orEmpty() {
		Assert.assertEquals("A", getFactory().empty().or("A"));
	}

	@Test
	public void orGetEmpty() {
		Assert.assertEquals("A", getFactory().empty().orGet(getFactory().of("A")));
	}

	@Test
	public void orGetValue() {
		Assert.assertEquals("B", getFactory().of("B").orGet(getFactory().of("A")));
	}

	@Test(expected = RuntimeException.class)
	public void orThrowEmpty() {
		getFactory().empty().orThrow(() -> new RuntimeException());
	}

	@Test
	public void orThrowValue() {
		Assert.assertEquals("B", getFactory().of("B").orThrow(() -> new RuntimeException()));
	}

	@Test
	public void orValue() {
		Assert.assertEquals("B", getFactory().of("B").or("A"));
	}

	@Test
	public void overrideEmpty() {
		final Object value = new Object();
		Assert.assertSame(value, getFactory().of(value).override(getFactory().empty()).get());
	}

	@Test
	public void overrideValue() {
		final Object value = new Object();
		Assert.assertSame(value, getFactory().of(new Object()).override(getFactory().of(value)).get());
	}

	@Test
	public void upcast() {
		final IOptional<Dummy> value = getFactory().of(new Dummy());
		final IOptional<Object> upcast = getFactory().upcast(value);
		Assert.assertSame(value.get(), upcast.get());
	}

	@Test
	public void value() {
		final Object value = new Object();
		final IOptional<Object> optional = getFactory().of(value);
		Assert.assertTrue(optional.isNotEmpty());
		Assert.assertFalse(optional.isEmpty());
		Assert.assertSame(value, optional.get());
	}

	@Test
	public void visitEmpty() {
		getFactory().empty().visit(v -> Assert.fail());
	}

	@Test
	public void visitValue() {
		final Object value = new Object();
		final Object[] output = new Object[] { null };
		getFactory().of(value).visit(v -> output[0] = v);
		Assert.assertEquals(value, output[0]);
	}
}
