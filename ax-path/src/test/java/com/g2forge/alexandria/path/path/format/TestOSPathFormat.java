package com.g2forge.alexandria.path.path.format;

import org.junit.Test;

import com.g2forge.alexandria.path.path.Path;
import com.g2forge.alexandria.test.HAssert;

public class TestOSPathFormat {
	@Test
	public void microsoftParse0() {
		HAssert.assertEquals(new Path<String>(""), OSPathFormat.Microsoft.toPath(""));
	}

	@Test
	public void microsoftParse1() {
		HAssert.assertEquals(new Path<String>("a"), OSPathFormat.Microsoft.toPath("a"));
	}

	@Test
	public void microsoftParseN() {
		HAssert.assertEquals(new Path<String>("a", "b", "c"), OSPathFormat.Microsoft.toPath("a\\b\\c"));
	}

	@Test
	public void microsoftRender0() {
		HAssert.assertEquals("", OSPathFormat.Microsoft.toString(Path.createEmpty()));
	}

	@Test
	public void microsoftRender1() {
		HAssert.assertEquals("a", OSPathFormat.Microsoft.toString(new Path<String>("a")));
	}

	@Test
	public void microsoftRenderN() {
		HAssert.assertEquals("a\\b\\c", OSPathFormat.Microsoft.toString(new Path<String>("a", "b", "c")));
	}

	@Test
	public void posixParse0() {
		HAssert.assertEquals(new Path<String>(""), OSPathFormat.POSIX.toPath(""));
	}

	@Test
	public void posixParse1() {
		HAssert.assertEquals(new Path<String>("a"), OSPathFormat.POSIX.toPath("a"));
	}

	@Test
	public void posixParseN() {
		HAssert.assertEquals(new Path<String>("a", "b", "c"), OSPathFormat.POSIX.toPath("a/b/c"));
	}

	@Test
	public void posixRender0() {
		HAssert.assertEquals("", OSPathFormat.POSIX.toString(Path.createEmpty()));
	}

	@Test
	public void posixRender1() {
		HAssert.assertEquals("a", OSPathFormat.POSIX.toString(new Path<String>("a")));
	}

	@Test
	public void posixRenderN() {
		HAssert.assertEquals("a/b/c", OSPathFormat.POSIX.toString(new Path<String>("a", "b", "c")));
	}
}
