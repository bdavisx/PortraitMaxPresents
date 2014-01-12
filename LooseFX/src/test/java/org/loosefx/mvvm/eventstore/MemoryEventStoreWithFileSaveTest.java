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
  private GenericDomainEventMessage<StubDomainEvent> event1;
  private GenericDomainEventMessage<StubDomainEvent> event2;
  private DomainEventStream eventStream;
  public static final String typeName = "TypeName";

  @Before
  public void setUp() {
    aggregateIdentifier = UUID.randomUUID();
  }

  @Test
  public void itStoresEventsInAnExistingList() throws Exception {
    Map<String, List<DomainEventMessage>> typeToEventListMap = new HashMap<>();
    List<DomainEventMessage> existingEventList = new ArrayList<>();
    typeToEventListMap.put( typeName, existingEventList );

    GenericDomainEventMessage<StubDomainEvent> existingEvent = new GenericDomainEventMessage<StubDomainEvent>(
      aggregateIdentifier, 0, new StubDomainEvent());
    existingEventList.add( existingEvent );

    createEventStream();

    MemoryEventStoreWithFileSave eventStore = new MemoryEventStoreWithFileSave( typeToEventListMap );
    eventStore.appendEvents( typeName, eventStream );

    makeSureListContainsEvent1And2( existingEventList );
  }

  @Test
  public void itStoresEventsInAnNewList() throws Exception {
    Map<String, List<DomainEventMessage>> typeToEventListMap = new HashMap<>();

    createEventStream();

    MemoryEventStoreWithFileSave eventStore = new MemoryEventStoreWithFileSave( typeToEventListMap );
    eventStore.appendEvents( typeName, eventStream );

    List<DomainEventMessage> existingEventList = typeToEventListMap.get( typeName );
    makeSureListContainsEvent1And2( existingEventList );
  }

  private void createEventStream() {
    event1 = new GenericDomainEventMessage<StubDomainEvent>( aggregateIdentifier, 1, new StubDomainEvent());
    event2 = new GenericDomainEventMessage<StubDomainEvent>( aggregateIdentifier, 2, new StubDomainEvent());
    eventStream = new SimpleDomainEventStream( event1, event2 );
  }

  private void makeSureListContainsEvent1And2( final List<DomainEventMessage> existingEventList ) {
    assertThat( existingEventList ).containsAll( Arrays.asList( new DomainEventMessage[]{ event1, event2 } ) );
  }

}


