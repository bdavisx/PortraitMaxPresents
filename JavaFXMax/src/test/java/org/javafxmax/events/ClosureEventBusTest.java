package org.javafxmax.events;

import org.junit.Test;

public class ClosureEventBusTest {

  @Test
  public void itShouldAllowEventsToBeRegisteredFor() throws Exception {
    ClosureEventBus.register( DummyEvent.class,  )
  }

  private static class DummyEvent {}

}
