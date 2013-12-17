package org.loosefx.events;

import junit.framework.Test;
import org.loosefx.distributors.ObjectDistributor;
import org.loosefx.distributors.TestEvents;

import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ClosureEventBusTest {
  // simple pass thru test for now, until it has more logic (if ever)
  @org.junit.Test
  public void itShouldPassThruCalls() throws Exception {
    ObjectDistributor objectDistributor = mock( ObjectDistributor.class );
    ClosureEventBus eventBus = new ClosureEventBus( objectDistributor );

    Consumer<TestEvents.EmptyBaseEvent> consumer = this::handle;
    Class<TestEvents.EmptyBaseEvent> messageClass = TestEvents.EmptyBaseEvent.class;
    eventBus.registerForMessageClassAndSubclasses( messageClass, consumer );
    eventBus.registerOnlyForMessageClass( messageClass, consumer );
    TestEvents.EmptyBaseEvent event = new TestEvents.EmptyBaseEvent();
    eventBus.send( event );
    eventBus.unregister( messageClass, consumer );

    verify( objectDistributor ).registerForMessageClassAndSubclasses( messageClass, consumer );
    verify( objectDistributor ).registerOnlyForMessageClass( messageClass, consumer );
    verify( objectDistributor ).send( event );
    verify( objectDistributor ).unregister( messageClass, consumer );
  }

  public void handle( TestEvents.EmptyBaseEvent event ) {}
}
