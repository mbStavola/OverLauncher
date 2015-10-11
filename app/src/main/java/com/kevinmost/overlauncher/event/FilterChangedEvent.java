package com.kevinmost.overlauncher.event;

public class FilterChangedEvent {
  public final String newFilter;

  public FilterChangedEvent(String newFilter) {
    this.newFilter = newFilter;
  }
}
