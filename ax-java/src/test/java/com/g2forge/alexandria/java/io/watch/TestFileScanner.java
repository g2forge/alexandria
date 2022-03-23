package com.g2forge.alexandria.java.io.watch;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.io.watch.FileScanner.Event;

import lombok.AccessLevel;
import lombok.Getter;

public class TestFileScanner {
	@Getter(value = AccessLevel.PROTECTED, lazy = true)
	private static final FileScanner scanner = new FileScanner(null, null, true, true);

	@Test
	public void simplifyMatch() {
		final Path a = Paths.get("A");
		final Path b = Paths.get("B");
		Assert.assertEquals(HCollection.asList(new Event(HCollection.asSet(a, b), false)), getScanner().simplify(HCollection.asList(new Event(HCollection.asSet(a), false), new Event(HCollection.asSet(b), false))));
	}

	@Test
	public void simplifyMismatch() {
		final Path a = Paths.get("A");
		final Path b = Paths.get("B");
		final List<Event> list = HCollection.asList(new Event(HCollection.asSet(a), false), new Event(HCollection.asSet(b), true));
		Assert.assertEquals(list, getScanner().simplify(list));
	}

	@Test
	public void simplifyThree() {
		final Path a = Paths.get("A");
		final Path b = Paths.get("B");
		final Path c = Paths.get("C");
		Assert.assertEquals(HCollection.asList(new Event(HCollection.asSet(a, b), false), new Event(HCollection.asSet(c), true)), getScanner().simplify(HCollection.asList(new Event(HCollection.asSet(a), false), new Event(HCollection.asSet(b), false), new Event(HCollection.asSet(c), true))));
	}

	@Test
	public void simplifyTrivial() {
		final FileScanner scanner = getScanner();
		Assert.assertNull(scanner.simplify(null));
		Assert.assertEquals(HCollection.emptyList(), scanner.simplify(HCollection.emptyList()));
		final List<Event> one = HCollection.asList(new Event(null, false));
		Assert.assertEquals(one, scanner.simplify(one));
	}
}
