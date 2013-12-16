package org.loosefx.events;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.junit.Assert;
import org.junit.Test;

public class EventContainerTest {

  @Test
  public void itShouldPublishAnEventWhenEventIsStored() {
    DummyEvent event = new DummyEvent();

    EventHandler handler = new EventHandler();
    EventBus eventBus = new EventBus();
    eventBus.register( handler );
    EventContainer eventContainer = new EventContainer( eventBus );

    eventContainer.addEvent( event );

    Assert.assertSame( event, handler.event );
  }

  private static class EventHandler {
    private DummyEvent event;

    @Subscribe
    public void handleDummyEvent( DummyEvent event ) {
      this.event = event;
    }
  }


  private static class DummyEvent {}
}
