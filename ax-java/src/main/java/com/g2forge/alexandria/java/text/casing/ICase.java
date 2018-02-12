package com.g2forge.alexandria.java.text.casing;

public interface ICase {
	public ICasedText fromString(String string);

	public String toString(ICasedText text);
}
