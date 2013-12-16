package org.loosefx.events;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

/** This should be on a one/aggregate basis. */
public class EventContainer {
  private final EventBus eventBus;

  @Inject
  public EventContainer( EventBus eventBus ) {
    this.eventBus = eventBus;
  }

  public void addEvent( Object event ) {
    eventBus.post( event );
  }
}
