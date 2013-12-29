package org.loosefx.eventsourcing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class AbstractAggregateRoot implements EventProvider, RegisterChildEntities {
  private final List<DomainEvent> appliedEvents;
  private final List<EntityEventProvider> childEventProviders;
  private final Map<Class, Consumer<DomainEvent>> registeredEvents;

  private UUID id;
  private AggregateVersion version;
  private AggregateVersion eventVersion;

  protected AbstractAggregateRoot() {
    appliedEvents = new ArrayList<>();
    childEventProviders = new ArrayList<>();
    registeredEvents = new HashMap<>();
  }

  public UUID getId() { return id; }
  protected void setId( final UUID id ) { this.id = id; }

  public AggregateVersion getVersion() { return version; }
  protected void setVersion( final AggregateVersion version ) { this.version = version; }

  public AggregateVersion getEventVersion() { return eventVersion; }
  protected void setEventVersion( AggregateVersion eventVersion ) { this.eventVersion = eventVersion; }

  @Override
  public void loadFromHistory( final Stream<DomainEvent> domainEvents ) {
    if( domainEvents.count() == 0 ) { return; }

    DomainEvent lastEvent = null;
    for( final Iterator<DomainEvent> iterator = domainEvents.iterator(); iterator.hasNext(); ) {
      DomainEvent domainEvent = iterator.next();
      apply(domainEvent.getClass(), domainEvent);
      lastEvent = domainEvent;
    }

    setVersion( lastEvent.getVersion() );
    setEventVersion( getVersion() );
  }

  @Override
  public Stream<DomainEvent> getChanges() {
    return Stream.concat( appliedEvents.stream(), getChildEvents() )
      .sorted( new DomainEventVersionComparator() );
  }

  void clearEvents() {
    childEventProviders.forEach( eventProvider -> eventProvider.clear());
    appliedEvents.clear();
  }

  @Override
  public void updateVersion( AggregateVersion version ) {
    this.version = version;
  }

  @Override
  public void registerChildEventProvider( final EntityEventProvider entityEventProvider ) {
    entityEventProvider.hookUpVersionProvider( this::getNewEventVersion );
    childEventProviders.add( entityEventProvider );
  }

  protected void registerEvent( final Class eventType, final Consumer<DomainEvent> eventHandler) {
    registeredEvents.put( eventType, eventHandler );
  }

  protected void apply( final DomainEvent domainEvent ) {
    domainEvent.setAggregateId( getId() );
    domainEvent.setVersion( getNewEventVersion() );
    apply( domainEvent.getClass(), domainEvent );
    appliedEvents.add( domainEvent );
  }

  private void apply( final Class eventType, final DomainEvent domainEvent ) {
    if( !registeredEvents.containsKey( eventType ) ) {
      throw new UnregisteredDomainEventException(
        String.format( "The requested domain event '%s' is not registered in '%s'", eventType.getName(),
          getClass().getName() ));
    }

    final Consumer<DomainEvent> handler = registeredEvents.get( eventType );
    handler.accept( domainEvent );
  }

  //private Stream<DomainEvent> getChildEventsAndUpdateEventVersion() {
  private Stream<DomainEvent> getChildEvents() {
    return childEventProviders.stream().flatMap( EntityEventProvider::getChanges );
  }

  private AggregateVersion getNewEventVersion() {
    setEventVersion( getEventVersion().incrementVersion() );
    return getEventVersion();
  }

}
