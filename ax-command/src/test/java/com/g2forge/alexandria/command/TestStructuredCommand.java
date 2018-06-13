package com.g2forge.alexandria.command;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.java.core.iface.ICommand;

public class TestStructuredCommand {
	@Test
	public void negative() throws Throwable {
		final IStructuredCommand.SubCommandBuilder builder = new IStructuredCommand.SubCommandBuilder();
		builder.add(invocation -> ICommand.SUCCESS, "a");
		final IStandardCommand command = builder.build();
		Assert.assertEquals(ICommand.FAIL, command.test(null, null, "x").getExitCode());
	}

	@Test
	public void positive() throws Throwable {
		final IStructuredCommand.SubCommandBuilder builder = new IStructuredCommand.SubCommandBuilder();
		builder.add(invocation -> invocation.getArguments().size(), "a", "aa", "aaa");
		builder.add(invocation -> -1, "b");
		final IStandardCommand command = builder.build();
		Assert.assertEquals(0, command.test(null, null, "a").getExitCode());
		Assert.assertEquals(1, command.test(null, null, "aaa", "b").getExitCode());
		Assert.assertEquals(2, command.test(null, null, "aa", "b", "c").getExitCode());
		Assert.assertEquals(-1, command.test(null, null, "b").getExitCode());
	}
}
