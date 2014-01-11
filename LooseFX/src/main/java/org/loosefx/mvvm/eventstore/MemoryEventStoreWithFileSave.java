package org.loosefx.mvvm.eventstore;

import org.axonframework.domain.DomainEventMessage;
import org.axonframework.domain.DomainEventStream;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.SnapshotEventStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryEventStoreWithFileSave implements EventStore, SnapshotEventStore {
  private final Map<String, List<DomainEventMessage>> typeToEventsListMap;

  public MemoryEventStoreWithFileSave( final Map<String, List<DomainEventMessage>> typeToEventsListMap ) {
    this.typeToEventsListMap = typeToEventsListMap;
  }

  public MemoryEventStoreWithFileSave() {
    this( new HashMap<>() );
  }

  @Override
  public void appendEvents( final String type, final DomainEventStream events ) {
    List eventsForType = getEventsForTypeList( type );
    while( events.hasNext() ) {
      eventsForType.add( events.next() );
    }
  }

  private List getEventsForTypeList( final String type ) {
    List eventsForType;
    if( typeToEventsListMap.containsKey( type ) ) {
      eventsForType = typeToEventsListMap.get( type );
    } else {
      eventsForType = new ArrayList();
      typeToEventsListMap.put( type, eventsForType );
    }
    return eventsForType;
  }

  @Override
  public void appendSnapshotEvent( final String type, final DomainEventMessage snapshotEvent ) {
    // TODO: change default implementation

  }

  @Override
  public DomainEventStream readEvents( final String type, final Object identifier ) {
    // TODO: change default implementation
    return null;
  }
}
