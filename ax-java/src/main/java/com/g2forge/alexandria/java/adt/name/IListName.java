package com.g2forge.alexandria.java.adt.name;

import java.util.List;

@FunctionalInterface
public interface IListName<T> {
	public List<T> getComponents();
}
