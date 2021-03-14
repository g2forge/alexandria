package com.g2forge.alexandria.command.main;

import java.io.InputStream;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Test;

import com.g2forge.alexandria.command.command.DispatchCommand;
import com.g2forge.alexandria.command.command.IStandardCommand;
import com.g2forge.alexandria.command.exit.Exit;
import com.g2forge.alexandria.command.exit.IExit;
import com.g2forge.alexandria.command.invocation.CommandInvocation;
import com.g2forge.alexandria.java.adt.name.IStringNamed;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class TestDispatchCommand {
	@Getter
	@RequiredArgsConstructor
	public static class NamedCommand implements IStandardCommand, IStringNamed {
		protected final String name;

		@Override
		public IExit invoke(CommandInvocation<InputStream, PrintStream> invocation) throws Throwable {
			return IStandardCommand.SUCCESS;
		}
	}

	@Test
	public void naming() throws Throwable {
		final IStandardCommand command = new DispatchCommand.NamingBuilder<NamedCommand>(IStringNamed::getName).command(new NamedCommand("a")).build();
		Assert.assertEquals(IStandardCommand.SUCCESS, command.test("a").getExit());
	}

	@Test
	public void negative() throws Throwable {
		final DispatchCommand.ManualBuilder builder = new DispatchCommand.ManualBuilder();
		builder.command(invocation -> IStandardCommand.SUCCESS, "a");
		final IStandardCommand command = builder.build();
		Assert.assertEquals(IStandardCommand.FAIL, command.test("x").getExit());
	}

	@Test
	public void positive() throws Throwable {
		final DispatchCommand.ManualBuilder builder = new DispatchCommand.ManualBuilder();
		builder.command(invocation -> new Exit(invocation.getArguments().size()), "a", "aa", "aaa");
		builder.command(invocation -> new Exit(-1), "b");
		final IStandardCommand command = builder.build();
		Assert.assertEquals(0, command.test("a").getExit().getCode());
		Assert.assertEquals(1, command.test("aaa", "b").getExit().getCode());
		Assert.assertEquals(2, command.test("aa", "b", "c").getExit().getCode());
		Assert.assertEquals(-1, command.test("b").getExit().getCode());
	}
}
