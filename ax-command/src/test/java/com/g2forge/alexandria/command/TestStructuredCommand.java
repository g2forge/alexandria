package com.g2forge.alexandria.command;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.command.exit.Exit;

public class TestStructuredCommand {
	@Test
	public void negative() throws Throwable {
		final IStructuredCommand.SubCommandBuilder builder = new IStructuredCommand.SubCommandBuilder();
		builder.add(invocation -> IStructuredCommand.SUCCESS, "a");
		final IStandardCommand command = builder.build();
		Assert.assertEquals(IStructuredCommand.FAIL, command.test(null, null, "x").getExit());
	}

	@Test
	public void positive() throws Throwable {
		final IStructuredCommand.SubCommandBuilder builder = new IStructuredCommand.SubCommandBuilder();
		builder.add(invocation -> new Exit(invocation.getArguments().size()), "a", "aa", "aaa");
		builder.add(invocation -> new Exit(-1), "b");
		final IStandardCommand command = builder.build();
		Assert.assertEquals(0, command.test(null, null, "a").getExit().getCode());
		Assert.assertEquals(1, command.test(null, null, "aaa", "b").getExit().getCode());
		Assert.assertEquals(2, command.test(null, null, "aa", "b", "c").getExit().getCode());
		Assert.assertEquals(-1, command.test(null, null, "b").getExit().getCode());
	}
}
