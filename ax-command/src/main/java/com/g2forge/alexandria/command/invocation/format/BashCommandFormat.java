package com.g2forge.alexandria.command.invocation.format;

import com.g2forge.alexandria.java.core.marker.ISingleton;
import com.g2forge.alexandria.java.text.quote.BashQuoteType;
import com.g2forge.alexandria.java.text.quote.QuoteControl;

public class BashCommandFormat implements ICommandFormat, ISingleton {
	protected static final BashCommandFormat INSTANCE = new BashCommandFormat();

	public static BashCommandFormat create() {
		return INSTANCE;
	}

	private BashCommandFormat() {}

	@Override
	public String quote(String argument) {
		return BashQuoteType.BashDoubleExpand.quoteAny(QuoteControl.Always, argument);
	}
}
