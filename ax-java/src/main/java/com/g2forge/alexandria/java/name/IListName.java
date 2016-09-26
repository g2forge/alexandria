package com.g2forge.alexandria.java.name;

import java.util.List;

@FunctionalInterface
public interface IListName<T> {
	public List<T> getComponents();
}
