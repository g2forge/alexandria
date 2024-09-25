package com.g2forge.alexandria.path.file.system;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.g2forge.alexandria.path.path.Path;
import com.g2forge.alexandria.test.HAssert;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RunWith(Parameterized.class)
@RequiredArgsConstructor
@Getter
public class TestOSFileSystem {
	@Parameters
	public static Collection<Object[]> data() {
		return Stream.of(OSFileSystem.values()).map(s -> new Object[] { s }).collect(Collectors.toList());
	}

	protected final IStandardFileSystem system;

	@Test
	public void empty() {
		HAssert.assertEquals(Path.createEmpty(), getSystem().normalize(Path.createEmpty()));
	}

	@Test
	public void isEscapeRootFalse() {
		HAssert.assertFalse(getSystem().isRootEscape(new Path<>(getSystem().getSelf(), getSystem().getSelf(), "a", getSystem().getParent(), getSystem().getSelf(), "b", getSystem().getSelf(), getSystem().getParent())));
	}

	@Test
	public void isEscapeRootTrue() {
		HAssert.assertTrue(getSystem().isRootEscape(new Path<>(getSystem().getSelf(), getSystem().getSelf(), "a", getSystem().getParent(), getSystem().getParent(), "b", getSystem().getParent(), getSystem().getSelf())));
	}

	@Test
	public void normalizeEscape() {
		HAssert.assertEquals(new Path<>(getSystem().getParent()), getSystem().normalize(new Path<>(getSystem().getSelf(), getSystem().getSelf(), "a", getSystem().getParent(), getSystem().getParent(), "b", getSystem().getParent(), getSystem().getSelf())));
	}

	@Test
	public void normalizeEscapeB() {
		HAssert.assertEquals(new Path<>(getSystem().getParent(), "b"), getSystem().normalize(new Path<>(getSystem().getSelf(), getSystem().getSelf(), "a", getSystem().getParent(), getSystem().getSelf(), getSystem().getParent(), getSystem().getSelf(), "b")));
	}

	@Test
	public void parentEnd() {
		HAssert.assertEquals(new Path<>("a"), getSystem().normalize(new Path<>("a", "b", getSystem().getParent())));
	}

	@Test
	public void parentMid() {
		HAssert.assertEquals(new Path<>("b"), getSystem().normalize(new Path<>("a", getSystem().getParent(), "b")));
	}

	@Test
	public void parentMulti() {
		HAssert.assertEquals(new Path<>(getSystem().getParent()), getSystem().normalize(new Path<>("a", getSystem().getParent(), getSystem().getParent())));
	}

	@Test
	public void parentSimple() {
		HAssert.assertEquals(new Path<>(getSystem().getParent()), getSystem().normalize(new Path<>(getSystem().getParent())));
	}

	@Test
	public void parentStart() {
		HAssert.assertEquals(new Path<>(getSystem().getParent(), "a"), getSystem().normalize(new Path<>(getSystem().getParent(), "a")));
	}

	@Test
	public void selfEnd() {
		HAssert.assertEquals(new Path<>("a"), getSystem().normalize(new Path<>("a", getSystem().getSelf())));
	}

	@Test
	public void selfMid() {
		HAssert.assertEquals(new Path<>("a", "b"), getSystem().normalize(new Path<>("a", getSystem().getSelf(), "b")));
	}

	@Test
	public void selfSimple() {
		HAssert.assertEquals(Path.createEmpty(), getSystem().normalize(new Path<>(getSystem().getSelf())));
	}

	@Test
	public void selfStart() {
		HAssert.assertEquals(new Path<>("a"), getSystem().normalize(new Path<>(getSystem().getSelf(), "a")));
	}
}
