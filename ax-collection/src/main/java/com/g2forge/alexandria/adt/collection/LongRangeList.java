package com.g2forge.alexandria.adt.collection;

import java.util.AbstractList;

public class LongRangeList extends AbstractList<Long> {
  protected final long base;
   
  protected final int size;
  
  public LongRangeList(long base, int size) {
    this.base = base;
    this.size = size;
  }

  @Override
  public Long get(int index) {
    return base + index;
  }

  @Override
  public int size() {
    return size;
  }
}
