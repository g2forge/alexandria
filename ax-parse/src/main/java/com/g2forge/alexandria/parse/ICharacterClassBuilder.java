package com.g2forge.alexandria.parse;

import com.g2forge.alexandria.java.close.ICloseable;

public interface ICharacterClassBuilder extends ICloseable {
	public ICharacterClassBuilder character(char character);

	public ICharacterClassBuilder range(char start, char end);

	public ICharacterClassBuilder named(NamedCharacterClass named);
}
