package com.portraitmax.presentation;

import org.loosefx.eventsourcing.AggregateVersion;
import org.loosefx.eventsourcing.DomainEvent;

import java.util.UUID;

public abstract class AbstractPresentationEvent implements DomainEvent {
  protected final UUID eventId;
  protected UUID presentationId;
  private AggregateVersion aggregateVersion;

  public AbstractPresentationEvent(
    final UUID eventId, UUID presentationId ) {
    this.eventId = eventId;
    this.presentationId = presentationId;
  }

  @Override public UUID getEventId() { return eventId; }

  @Override public UUID getAggregateId() { return getPresentationId(); }

  @Override public void setAggregateId( final UUID aggregateId ) { presentationId = aggregateId; }

  @Override public AggregateVersion getVersion() { return aggregateVersion; }

  @Override public void setVersion( final AggregateVersion version ) { this.aggregateVersion = version; }

  public UUID getPresentationId() {return presentationId;}
}
