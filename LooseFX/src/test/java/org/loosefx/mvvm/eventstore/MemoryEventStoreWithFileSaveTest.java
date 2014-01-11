package org.loosefx.mvvm.eventstore;

import org.axonframework.domain.DomainEventMessage;
import org.axonframework.domain.DomainEventStream;
import org.axonframework.domain.GenericDomainEventMessage;
import org.axonframework.domain.SimpleDomainEventStream;
import static org.fest.assertions.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MemoryEventStoreWithFileSaveTest {
  private UUID aggregateIdentifier;

  @Before
  public void setUp() {
    aggregateIdentifier = UUID.randomUUID();
  }

  @Test
  public void itStoresEventsInAnExistingList() throws Exception {
    final String type = "TypeName";
    List<DomainEventMessage> existingEventList = new ArrayList<>();
    Map<String, List<DomainEventMessage>> typeToEventListMap = new HashMap<>();
    typeToEventListMap.put( type, existingEventList );

    GenericDomainEventMessage<StubDomainEvent> existingEvent = new GenericDomainEventMessage<StubDomainEvent>(
      aggregateIdentifier, 0, new StubDomainEvent());
    existingEventList.add( existingEvent );

    GenericDomainEventMessage<StubDomainEvent> event1 = new GenericDomainEventMessage<StubDomainEvent>(
      aggregateIdentifier, 1, new StubDomainEvent());
    GenericDomainEventMessage<StubDomainEvent> event2 = new GenericDomainEventMessage<StubDomainEvent>(
      aggregateIdentifier, 2, new StubDomainEvent());
    DomainEventStream stream = new SimpleDomainEventStream( event1, event2 );

    MemoryEventStoreWithFileSave eventStore = new MemoryEventStoreWithFileSave( typeToEventListMap );
    eventStore.appendEvents( type, stream );

    assertThat( existingEventList ).containsAll( Arrays.asList( new DomainEventMessage[] { event1, event2 } ) );
  }
}


