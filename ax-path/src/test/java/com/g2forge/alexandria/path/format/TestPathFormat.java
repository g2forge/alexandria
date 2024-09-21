package com.g2forge.alexandria.path.format;

import org.junit.Test;

import com.g2forge.alexandria.path.Path;
import com.g2forge.alexandria.path.format.PathFormat;
import com.g2forge.alexandria.test.HAssert;

public class TestPathFormat {
	@Test
	public void microsoft0() {
		HAssert.assertEquals("", PathFormat.Microsoft.toString(Path.createEmpty()));
	}

	@Test
	public void microsoft1() {
		HAssert.assertEquals("a", PathFormat.Microsoft.toString(new Path<String>("a")));
	}

	@Test
	public void microsoftN() {
		HAssert.assertEquals("a\\b\\c", PathFormat.Microsoft.toString(new Path<String>("a", "b", "c")));
	}

	@Test
	public void posix0() {
		HAssert.assertEquals("", PathFormat.POSIX.toString(Path.createEmpty()));
	}

	@Test
	public void posix1() {
		HAssert.assertEquals("a", PathFormat.POSIX.toString(new Path<String>("a")));
	}

	@Test
	public void posixN() {
		HAssert.assertEquals("a/b/c", PathFormat.POSIX.toString(new Path<String>("a", "b", "c")));
	}
}
