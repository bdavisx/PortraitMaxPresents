package org.loosefx.mvvm.eventstore;

import org.axonframework.domain.DomainEventMessage;
import org.axonframework.domain.DomainEventStream;
import org.axonframework.domain.SimpleDomainEventStream;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.SnapshotEventStore;
import org.axonframework.serializer.Serializer;
import org.axonframework.serializer.xml.XStreamSerializer;

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
    private final Serializer eventSerializer;
    private final Comparator<DomainEventMessage> comparator =
        new DomainEventMessageReverseSequenceNumberComparator();

    private final Map<String, List<DomainEventMessage>> typeToRegularEventsListMap;
    private final Map<String, List<DomainEventMessage>> typeToSnapshotEventsListMap;

    /** Creates a default set of maps using HashMap. */
    public MemoryEventStoreWithFileSave( final Serializer eventSerializer ) {
        this( new HashMap<>(), new HashMap<>(), eventSerializer );
    }

    /*  if there are any more parameters, probably need to split up class; otherwise need to combine the map
        initialization into a factory. */
    public MemoryEventStoreWithFileSave( final Map<String, List<DomainEventMessage>> typeToRegularEventsListMap,
        final Map<String, List<DomainEventMessage>> typeToSnapshotEventsListMap,
        final Serializer eventSerializer ) {
        this.typeToRegularEventsListMap = typeToRegularEventsListMap;
        this.typeToSnapshotEventsListMap = typeToSnapshotEventsListMap;
        this.eventSerializer = eventSerializer;
    }

    public MemoryEventStoreWithFileSave( final Map<String, List<DomainEventMessage>> typeToRegularEventsListMap,
        final Map<String, List<DomainEventMessage>> typeToSnapshotEventsListMap ) {
        this( typeToRegularEventsListMap, typeToSnapshotEventsListMap, new XStreamSerializer() );
    }

    /** Creates a default set of maps using HashMap and XStreamSerializer. */
    public MemoryEventStoreWithFileSave() { this( new XStreamSerializer() ); }

    @Override
    public void appendEvents( final String type, final DomainEventStream events ) {
        if( !events.hasNext() ) { return; }

        final List<DomainEventMessage> eventsForType = getOrCreateRegularEventListForType( type );
        while( events.hasNext() ) {
            eventsForType.add( events.next() );
        }
    }

    @Override
    public void appendSnapshotEvent( final String type, final DomainEventMessage snapshotEvent ) {
        final List<DomainEventMessage> snapshotEventsForType = getOrCreateSnapshotEventListForType( type );
        snapshotEventsForType.add( snapshotEvent );
    }

    @Override
    public DomainEventStream readEvents( final String type, final Object identifier ) {
        return new SimpleDomainEventStream( buildEventsList( type, identifier ) );
    }

    private List<DomainEventMessage> buildEventsList( final String type, final Object identifier ) {
        final List<DomainEventMessage> eventsForStream = new ArrayList<DomainEventMessage>();

        final List<DomainEventMessage> snapshotEventListForType =
            getEventListForType( type, typeToSnapshotEventsListMap );
        final List<DomainEventMessage> regularEventListForType =
            getEventListForType( type, typeToRegularEventsListMap );
        if( snapshotEventListForType == null && regularEventListForType == null ) { return eventsForStream; }

        final Optional<DomainEventMessage> lastSnapshotEventOptional = getLastSnapshotEventOptional(
            identifier, snapshotEventListForType );
        final long startingSequenceNumber = lastSnapshotEventOptional.isPresent() ?
            lastSnapshotEventOptional.get().getSequenceNumber() : -1;
        if( lastSnapshotEventOptional.isPresent() ) { eventsForStream.add( lastSnapshotEventOptional.get() ); }

        Stream<DomainEventMessage> domainEventsAfterSnapshot = null;
        if( regularEventListForType != null ) {
            domainEventsAfterSnapshot =
                regularEventListForType.stream().filter( createFilterForAggregateIdentifier( identifier ) )
                    .filter( eventMessage -> eventMessage.getSequenceNumber() > startingSequenceNumber );
        }

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

    private static class DomainEventMessageReverseSequenceNumberComparator
        implements Comparator<DomainEventMessage> {
        @Override
        public int compare( final DomainEventMessage lhs, final DomainEventMessage rhs ) {
            if( lhs.getSequenceNumber() == rhs.getSequenceNumber() ) { return 0; }
            if( lhs.getSequenceNumber() < rhs.getSequenceNumber() ) { return 1; }

            return -1;
        }
    }
}
