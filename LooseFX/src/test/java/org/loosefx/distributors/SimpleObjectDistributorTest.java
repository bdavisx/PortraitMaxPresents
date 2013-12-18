package org.loosefx.distributors;

import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SimpleObjectDistributorTest {
  private List<TestEvents.EmptyBaseEvent> events = new ArrayList<>();

  @Test
  public void itShouldSendSubclasses() throws Exception {
    SimpleObjectDistributor distributor = new SimpleObjectDistributor( new ConsumerSelectorFactory() );
    distributor.registerForMessageClassAndSubclasses( TestEvents.EmptyBaseEvent.class, this::handle );
    TestEvents.EmptyBaseEvent baseEvent = new TestEvents.EmptyBaseEvent();
    TestEvents.EmptyInheritedEvent inheritedEvent = new TestEvents.EmptyInheritedEvent();
    distributor.send( baseEvent );
    distributor.send( inheritedEvent );

    assertThat( events ).contains( baseEvent );
    assertThat( events ).contains( inheritedEvent );
  }

  @Test
  public void itShouldNotSendIfUnregistered() throws Exception {
    SimpleObjectDistributor distributor = new SimpleObjectDistributor( new ConsumerSelectorFactory() );
    distributor.registerForMessageClassAndSubclasses( TestEvents.EmptyBaseEvent.class, this::handle );
    distributor.unregister( TestEvents.EmptyBaseEvent.class, this::handle );
    TestEvents.EmptyBaseEvent baseEvent = new TestEvents.EmptyBaseEvent();
    TestEvents.EmptyInheritedEvent inheritedEvent = new TestEvents.EmptyInheritedEvent();
    distributor.send( baseEvent );
    distributor.send( inheritedEvent );

    assertThat( events ).doesNotContain( baseEvent );
    assertThat( events ).doesNotContain( inheritedEvent );
  }

  private void handle( TestEvents.EmptyBaseEvent event ) {
    events.add( event );
  }

}
