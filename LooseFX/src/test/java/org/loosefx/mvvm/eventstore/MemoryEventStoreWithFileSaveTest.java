package org.loosefx.mvvm.eventstore;

import org.axonframework.domain.DomainEventMessage;
import org.axonframework.domain.DomainEventStream;
import org.axonframework.domain.GenericDomainEventMessage;
import org.axonframework.domain.SimpleDomainEventStream;
import static org.fest.assertions.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MemoryEventStoreWithFileSaveTest {
  private UUID aggregateIdentifier;
  private DomainEventStream eventStream;
  public static final String typeName = "TypeName";

  @Before
  public void setUp() {
    aggregateIdentifier = UUID.randomUUID();
  }

  @Test
  public void itStoresEventsInAnExistingList() throws Exception {
    Map<String, List<DomainEventMessage>> typeToEventListMap = new HashMap<>();
    Map<String, List<DomainEventMessage>> typeToSnapshotEventListMap = new HashMap<>();
    List<DomainEventMessage> existingEventList = new ArrayList<>();
    typeToEventListMap.put( typeName, existingEventList );

    GenericDomainEventMessage<StubDomainEvent> existingEvent = new GenericDomainEventMessage<StubDomainEvent>(
      aggregateIdentifier, 0, new StubDomainEvent( 0 ));
    existingEventList.add( existingEvent );

    List<DomainEventMessage> regularEvents = createRegularEvents( 1, 2 );
    createEventStream( regularEvents );

    MemoryEventStoreWithFileSave eventStore = new MemoryEventStoreWithFileSave( typeToEventListMap,
      typeToSnapshotEventListMap) ;
    eventStore.appendEvents( typeName, eventStream );

    makeSureListContainsEvents( regularEvents, existingEventList );
  }

  @Test
  public void itStoresEventsInAnNewList() throws Exception {
    Map<String, List<DomainEventMessage>> typeToEventListMap = new HashMap<>();
    Map<String, List<DomainEventMessage>> typeToSnapshotEventListMap = new HashMap<>();

    final List<DomainEventMessage> regularEvents = createRegularEvents( 1, 2 );
    createEventStream( regularEvents );

    MemoryEventStoreWithFileSave eventStore = new MemoryEventStoreWithFileSave( typeToEventListMap,
      typeToSnapshotEventListMap) ;
    eventStore.appendEvents( typeName, eventStream );

    List<DomainEventMessage> existingEventList = typeToEventListMap.get( typeName );
    makeSureListContainsEvents( existingEventList, regularEvents );
  }

  @Test
  public void itStoresSnapshotEventInAnNewList() throws Exception {
    Map<String, List<DomainEventMessage>> typeToEventListMap = new HashMap<>();
    Map<String, List<DomainEventMessage>> typeToSnapshotEventListMap = new HashMap<>();

    final List<DomainEventMessage> regularEvents = createRegularEvents( 1, 2 );
    createEventStream( regularEvents );
    final DomainEventMessage snapshotEvent = createSnapshotEvent( 3 );

    MemoryEventStoreWithFileSave eventStore = new MemoryEventStoreWithFileSave( typeToEventListMap,
      typeToSnapshotEventListMap) ;
    eventStore.appendEvents( typeName, eventStream );
    eventStore.appendSnapshotEvent( typeName, snapshotEvent );

    List<DomainEventMessage> existingEventList = typeToSnapshotEventListMap.get( typeName );
    assertThat( existingEventList ).contains( snapshotEvent );
  }

  private List<DomainEventMessage> createRegularEvents( long startingSequenceNumber, final int numberOfEventsToCreate ) {
    List<DomainEventMessage> events = new ArrayList<>();
    final long endingSequenceNumber = numberOfEventsToCreate + startingSequenceNumber;
    for( long currentSequenceNumber = startingSequenceNumber; currentSequenceNumber < endingSequenceNumber;
         currentSequenceNumber++ ) {
      events.add( new GenericDomainEventMessage<StubDomainEvent>( aggregateIdentifier, currentSequenceNumber,
        new StubDomainEvent( currentSequenceNumber ) ) );
    }
    return events;
  }

  @Test
  public void itReturnsRegularEvents() throws Exception {
    Map<String, List<DomainEventMessage>> typeToEventListMap = new HashMap<>();
    Map<String, List<DomainEventMessage>> typeToSnapshotEventListMap = new HashMap<>();
    List<DomainEventMessage> existingEventList = new ArrayList<>();
    typeToEventListMap.put( typeName, existingEventList );

    GenericDomainEventMessage<StubDomainEvent> existingEvent = new GenericDomainEventMessage<StubDomainEvent>(
      aggregateIdentifier, 0, new StubDomainEvent( 0 ));
    existingEventList.add( existingEvent );
    existingEventList.addAll( createRegularEvents( 1, 2 ) );

    MemoryEventStoreWithFileSave eventStore = new MemoryEventStoreWithFileSave( typeToEventListMap,
      typeToSnapshotEventListMap) ;

    final DomainEventStream domainEventStream = eventStore.readEvents( typeName, aggregateIdentifier );

    makeSureListContainsEvents( existingEventList, domainEventStream );
  }

  @Test
  public void itReturnsRegularEventsAndSnapshot() throws Exception {
    Map<String, List<DomainEventMessage>> typeToEventListMap = new HashMap<>();
    Map<String, List<DomainEventMessage>> typeToSnapshotEventListMap = new HashMap<>();
    List<DomainEventMessage> existingEventList = new ArrayList<>();
    List<DomainEventMessage> existingSnapshotEventList = new ArrayList<>();
    typeToEventListMap.put( typeName, existingEventList );
    typeToSnapshotEventListMap.put( typeName, existingSnapshotEventList );

    GenericDomainEventMessage<StubDomainEvent> existingEvent = new GenericDomainEventMessage<StubDomainEvent>(
      aggregateIdentifier, 0, new StubDomainEvent( 0 ));
    existingEventList.add( existingEvent );
    existingEventList.addAll( createRegularEvents( 1, 2 ) );
    final DomainEventMessage snapshotEvent = createSnapshotEvent( 3 );
    existingSnapshotEventList.add( snapshotEvent );
    final List<DomainEventMessage> regularEventsAfterSnapshot = createRegularEvents( 4, 5 );
    existingEventList.addAll( regularEventsAfterSnapshot );

    MemoryEventStoreWithFileSave eventStore = new MemoryEventStoreWithFileSave( typeToEventListMap,
      typeToSnapshotEventListMap) ;

    final DomainEventStream domainEventStream = eventStore.readEvents( typeName, aggregateIdentifier );

    List<DomainEventMessage> eventsAfterSnapshotIncludingSnapshot = new ArrayList<>();
    eventsAfterSnapshotIncludingSnapshot.add( snapshotEvent );
    eventsAfterSnapshotIncludingSnapshot.addAll( regularEventsAfterSnapshot );

    makeSureListContainsEvents( eventsAfterSnapshotIncludingSnapshot, domainEventStream );
  }

  private DomainEventMessage createSnapshotEvent( long sequenceNumber ) {
    return new GenericDomainEventMessage( aggregateIdentifier, sequenceNumber,
      new StubSnapshotDomainEvent( sequenceNumber ) );
  }

  private void createEventStream( final List<DomainEventMessage> events ) {
    eventStream = new SimpleDomainEventStream( events );
  }

  private void makeSureListContainsEvents( final List<DomainEventMessage> expectedEvents,
    final List<DomainEventMessage> listToCheck ) {
    assertThat( listToCheck ).containsAll( expectedEvents );
  }

  private void makeSureListContainsEvents( final List<DomainEventMessage> expectedEvents,
    final DomainEventStream domainEventStream ) {

    List<DomainEventMessage> listToCheck = new ArrayList<>();
    while( domainEventStream.hasNext() ) { listToCheck.add( domainEventStream.next() ); }

    makeSureListContainsEvents( expectedEvents, listToCheck );
  }

}


