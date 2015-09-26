package com.kevinmost.overlauncher.event;

public class FilterChangedEvent {
  private final String oldFilter;
  public final String newFilter;

  public FilterChangedEvent(String oldFilter, String newFilter) {
    this.oldFilter = oldFilter;
    this.newFilter = newFilter;
  }
}
