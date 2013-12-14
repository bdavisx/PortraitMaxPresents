package com.portraitmax.configuration;

import com.google.common.eventbus.EventBus;

public class MainInjector {
  private final EventBus eventBus;

  public MainInjector() {
    eventBus = new EventBus();
  }

  public EventBus eventBus() { return eventBus; }


}
