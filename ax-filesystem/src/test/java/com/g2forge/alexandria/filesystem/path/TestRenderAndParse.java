package com.g2forge.alexandria.filesystem.path;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestRenderAndParse extends ATestGeneric {
	@Parameters
	public static Collection<Object[]> parameters() {
		final Collection<Object[]> retVal = new ArrayList<>();
		retVal.add(new Object[] { create(null, "foo"), "foo", null });
		retVal.add(new Object[] { create("", "foo", "bar"), "/foo/bar", null });
		retVal.add(new Object[] { create("", "foo"), "/foo", null });
		retVal.add(new Object[] { create(null, "foo", "bar"), "foo//bar", "foo/bar" });
		retVal.add(new Object[] { create(null), "", null });
		retVal.add(new Object[] { create(""), "/", null });
		return retVal;
	}

	@Parameter(0)
	public GenericPath path;

	@Parameter(1)
	public String input;

	@Parameter(2)
	public String expected;

	@Test
	public void parse() {
		final GenericPath path = parse(input);
		Assert.assertEquals(this.path, path);
	}

	@Test
	public void render() {
		Assert.assertEquals(expected == null ? input : expected, path.toString());
	}
}
