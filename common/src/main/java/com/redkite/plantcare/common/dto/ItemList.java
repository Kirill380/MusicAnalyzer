package com.redkite.plantcare.common.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ItemList<T> {

  private final List<T> items;

  private final Long totalCount;

  public ItemList(List<T> items, Long totalCount) {
    this.items = new ArrayList<>(items);
    this.totalCount = totalCount;
  }
}
