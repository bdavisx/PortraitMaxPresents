package org.loosefx.mvvm.eventstore;

import org.axonframework.domain.DomainEventMessage;
import org.axonframework.domain.DomainEventStream;
import org.axonframework.domain.SimpleDomainEventStream;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.SnapshotEventStore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MemoryEventStoreWithFileSave implements EventStore, SnapshotEventStore {
  private final Comparator<DomainEventMessage> comparator = new DomainEventMessageReverseSequenceNumberComparator();

  private final Map<String, List<DomainEventMessage>> typeToRegularEventsListMap;
  private final Map<String, List<DomainEventMessage>> typeToSnapshotEventsListMap;

  public MemoryEventStoreWithFileSave( final Map<String, List<DomainEventMessage>> typeToRegularEventsListMap,
    final Map<String, List<DomainEventMessage>> typeToSnapshotEventsListMap ) {
    this.typeToRegularEventsListMap = typeToRegularEventsListMap;
    this.typeToSnapshotEventsListMap = typeToSnapshotEventsListMap;
  }

  /** Creates a default set of maps using HashMap. */
  public MemoryEventStoreWithFileSave() { this( new HashMap<>(), new HashMap<>() ); }

  @Override
  public void appendEvents( final String type, final DomainEventStream events ) {
    if (!events.hasNext()) { return; }

    List<DomainEventMessage> eventsForType = getOrCreateRegularEventListForType( type );
    while( events.hasNext() ) {
      eventsForType.add( events.next() );
    }
  }

  @Override
  public void appendSnapshotEvent( final String type, final DomainEventMessage snapshotEvent ) {
    List<DomainEventMessage> snapshotEventsForType = getOrCreateSnapshotEventListForType( type );
    snapshotEventsForType.add( snapshotEvent );
  }

  @Override
  public DomainEventStream readEvents( final String type, final Object identifier ) {
    return new SimpleDomainEventStream( buildEventsList( type, identifier ) );
  }

  // TODO: need tests for snapshots and reads****************************************

  private List<DomainEventMessage> buildEventsList( final String type, final Object identifier ) {
    final List<DomainEventMessage> eventsForStream = new ArrayList<DomainEventMessage>();

    final List<DomainEventMessage> snapshotEventListForType = getEventListForType( type, typeToSnapshotEventsListMap );
    final List<DomainEventMessage> regularEventListForType = getEventListForType( type, typeToRegularEventsListMap );
    if( snapshotEventListForType == null && regularEventListForType == null ) { return eventsForStream; }

    Optional<DomainEventMessage> lastSnapshotEventOptional = getLastSnapshotEventOptional(
      identifier, snapshotEventListForType );
    long startingSequenceNumber = lastSnapshotEventOptional.isPresent() ?
      lastSnapshotEventOptional.get().getSequenceNumber() : -1;

    Stream<DomainEventMessage> domainEventsAfterSnapshot = null;
    if( regularEventListForType != null ) {
      domainEventsAfterSnapshot =
        regularEventListForType.stream().filter( createFilterForAggregateIdentifier( identifier ) )
          .filter( eventMessage -> eventMessage.getSequenceNumber() > startingSequenceNumber );
    }

    if( lastSnapshotEventOptional.isPresent() ) { eventsForStream.add( lastSnapshotEventOptional.get() ); }
    eventsForStream.addAll( domainEventsAfterSnapshot.collect( Collectors.toList() ) );
    return eventsForStream;
  }

  private Optional<DomainEventMessage> getLastSnapshotEventOptional( final Object identifier,
    final List<DomainEventMessage> snapshotEventListForType ) {
    Optional<DomainEventMessage> lastSnapshotEventOptional = Optional.empty();
    if( snapshotEventListForType != null ) {
      lastSnapshotEventOptional = snapshotEventListForType.stream().filter(
        createFilterForAggregateIdentifier( identifier ) ).sorted( comparator ).findFirst();
    }
    return lastSnapshotEventOptional;
  }

  private Predicate<? super DomainEventMessage> createFilterForAggregateIdentifier( final Object identifier ) {
    return eventMessage -> eventMessage.getAggregateIdentifier().equals( identifier );
  }

  private List<DomainEventMessage> getOrCreateSnapshotEventListForType( final String type ) {
    return getOrCreateEventListForType( type, typeToSnapshotEventsListMap );
  }

  private List<DomainEventMessage> getOrCreateRegularEventListForType( final String type ) {
    return getOrCreateEventListForType( type, typeToRegularEventsListMap );
  }

  private List<DomainEventMessage> getOrCreateEventListForType( final String type,
    final Map<String, List<DomainEventMessage>> typeToEventsListMap ) {
    List eventsForType = getEventListForType( type, typeToEventsListMap );
    if( eventsForType == null ) {
      eventsForType = new ArrayList();
      typeToEventsListMap.put( type, eventsForType );
    }
    return eventsForType;
  }

  private List<DomainEventMessage> getEventListForType( final String type,
    final Map<String, List<DomainEventMessage>> typeToEventsListMap ) {
    return typeToEventsListMap.get( type );
  }

  private static class DomainEventMessageReverseSequenceNumberComparator implements Comparator<DomainEventMessage> {
    @Override
    public int compare( final DomainEventMessage lhs, final DomainEventMessage rhs ) {
      if( lhs.getSequenceNumber() == rhs.getSequenceNumber() ) { return 0; }
      if( lhs.getSequenceNumber() < rhs.getSequenceNumber() ) { return 1; }

      return -1;
    }
  }
}
