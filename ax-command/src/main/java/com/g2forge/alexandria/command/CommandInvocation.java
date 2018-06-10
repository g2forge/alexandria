package com.g2forge.alexandria.command;

import java.util.List;

import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommandInvocation {
	public static final CommandInvocation of(String... args) {
		return new CommandInvocation(HCollection.asList(args));
	}

	protected final List<String> arguments;
}
