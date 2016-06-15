package com.g2forge.alexandria.adt.collection;

import java.util.AbstractList;

public class IntegerRangeList extends AbstractList<Integer> {
  protected final int base;
   
  protected final int size;
  
  public IntegerRangeList(int base, int size) {
    this.base = base;
    this.size = size;
  }

  @Override
  public Integer get(int index) {
    return base + index;
  }

  @Override
  public int size() {
    return size;
  }
}
