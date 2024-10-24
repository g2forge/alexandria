package com.g2forge.alexandria.command.invocation;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.test.HAssert;

public class TestCommandInvocation {
	@Test
	public void getArgumentAsPathAbsolute() {
		final Path absolutePath = CommandInvocation.of().getWorking().toAbsolutePath();
		HAssert.assertEquals(absolutePath, new CommandInvocation<>(null, HCollection.asList(absolutePath.toString()), null, null, null).getArgumentAsPath(0));
	}

	@Test
	public void getArgumentAsPathNull() {
		HAssert.assertNull(new CommandInvocation<>(null, HCollection.asList((String) null), null, null, null).getArgumentAsPath(0));
	}

	@Test
	public void getArgumentAsPathRelative() {
		final Path relativePath = CommandInvocation.of().getWorking();
		HAssert.assertEquals(relativePath, new CommandInvocation<>(null, HCollection.asList(relativePath.toString()), null, null, null).getArgumentAsPath(0));
	}

	@Test
	public void getArgumentAsPathWorkingRelative() {
		final Path relativePath = Paths.get("relative");
		final Path working = CommandInvocation.of().getWorking();
		HAssert.assertEquals(working.resolve(relativePath), new CommandInvocation<>(null, HCollection.asList(relativePath.toString()), null, working, null).getArgumentAsPath(0));
	}
}
