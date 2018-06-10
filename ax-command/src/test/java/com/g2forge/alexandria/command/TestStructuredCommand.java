package com.g2forge.alexandria.command;

import org.junit.Assert;
import org.junit.Test;

public class TestStructuredCommand {
	@Test
	public void test() throws Throwable {
		final IStructuredCommand.SubCommandBuilder builder = new IStructuredCommand.SubCommandBuilder();
		builder.add(invocation -> invocation.getArguments().size(), "a", "aa", "aaa");
		builder.add(invocation -> -1, "b");
		final IStandardCommand command = builder.build();
		Assert.assertEquals(0, command.invoke(CommandInvocation.of("a")));
		Assert.assertEquals(1, command.invoke(CommandInvocation.of("aaa", "b")));
		Assert.assertEquals(2, command.invoke(CommandInvocation.of("aa", "b", "c")));
		Assert.assertEquals(-1, command.invoke(CommandInvocation.of("b")));
	}
}
